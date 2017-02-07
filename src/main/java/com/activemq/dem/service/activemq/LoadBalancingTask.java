package com.activemq.dem.service.activemq;

import com.activemq.dem.service.activemq.config.JmsTemplateNames;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Log4j
@Component
public class LoadBalancingTask
{
	private static final long INTERVAL = 10_000; // 10 seconds

	@Autowired
	private JmsListenerEndpointRegistry registry;

	@Autowired
	@Qualifier(value = JmsTemplateNames.FROZEN_JMS_MESSAGE_TEMPLATE)
	private JmsTemplate frozenJmsMessageTemplate;

	@Autowired
	@Qualifier(value = JmsTemplateNames.CHECKIN_JMS_MESSAGE_TEMPLATE)
	private JmsTemplate checkinJmsMessageTemplate;

	List<JmsTemplate> jmsTemplates = new ArrayList<>();
	Collection<MessageListenerContainer> listenerContainers;

	@PostConstruct
	public void initTemplate()
	{
		jmsTemplates.addAll(Arrays.asList(frozenJmsMessageTemplate, checkinJmsMessageTemplate));
		listenerContainers = registry.getListenerContainers();

	}

	@Scheduled(fixedDelay = INTERVAL)
	public void execute() throws IOException
	{
		log.debug("Execute load balancing task");
		boolean priorityMessages = hasHighPriorityMessages();
		listenerContainers.forEach(listener ->
		{
			DefaultMessageListenerContainer messageListener = (DefaultMessageListenerContainer) listener;
			messageListener.setIdleTaskExecutionLimit(10);
			messageListener.setIdleConsumerLimit(10);
		});
		balanceLoading(listenerContainers, priorityMessages);
		log.debug("Finished load balancing task");
	}

	private void balanceLoading(Collection<MessageListenerContainer> listenerContainers, boolean priorityMessages)
	{
		if (priorityMessages)
		{
			balanceHighLoading(listenerContainers);
		}
		else
		{
			balanceNormalLoading(listenerContainers);
		}
	}

	private void balanceNormalLoading(Collection<MessageListenerContainer> listenerContainers)
	{
		listenerContainers.forEach(l -> balanceNormalLoading((DefaultMessageListenerContainer) l));
	}

	private void balanceHighLoading(Collection<MessageListenerContainer> listenerContainers)
	{
		listenerContainers.forEach(l -> balanceHighLoading((DefaultMessageListenerContainer) l));
	}

	private void balanceHighLoading(DefaultMessageListenerContainer listener)
	{
		String destinationName = listener.getDestinationName();
		log.info(getLogInfo(destinationName, listener));
		if (MessageQueuesName.FROZEN_MESSAGE_QUEUE.equalsIgnoreCase(destinationName))
		{
			return;
		}
		//listener.stop();
		int min = 1;
		int currentMax = listener.getMaxConcurrentConsumers();
		if(currentMax == min)
		{
			return;
		}
		log.info(String.format("Change settings of listener '%s': max=%s > %s",
			destinationName,
			currentMax, min));
		listener.setMaxConcurrentConsumers(min);
	}

	private boolean hasHighPriorityMessages()
	{
		return jmsTemplates
			.stream().filter(jmsTemplate -> jmsTemplate.getPriority() > 8)
			.anyMatch(template -> template.browse((s, b) -> b.getEnumeration().hasMoreElements()));
	}

	private void balanceNormalLoading(DefaultMessageListenerContainer listener)
	{
		String destinationName = listener.getDestinationName();
		log.info(getLogInfo(destinationName, listener));
//		if (!listener.isRunning()){
//			listener.start();
//		}
		int max = 10;
		int currentMax = listener.getMaxConcurrentConsumers();
		if(currentMax == max)
		{
			return;
		}
		log.info(String.format("Change settings of listener '%s': max=%s > %s",
			destinationName,
			currentMax, max));
		listener.setMaxConcurrentConsumers(max);
	}

	private String getLogInfo(String destinationName, DefaultMessageListenerContainer l)
	{
		return String.format("[%s]: active - %s, scheduled - %s (concurent %s - max concurent %s), idle consumer %s, idle task %s, paused task %s",
			destinationName,
			l.getActiveConsumerCount(),
			l.getScheduledConsumerCount(),
			l.getConcurrentConsumers(),
			l.getMaxConcurrentConsumers(),
			l.getIdleConsumerLimit(),
			l.getIdleTaskExecutionLimit(),
			l.getPausedTaskCount());
	}
}
