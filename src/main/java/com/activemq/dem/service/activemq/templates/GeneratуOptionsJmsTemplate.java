package com.activemq.dem.service.activemq.templates;

import com.activemq.dem.service.activemq.config.JmsTemplateNames;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

@Log4j
@Component
public class GeneratуOptionsJmsTemplate
{
	@Value("${pp.jms.generateoptions.priority}")
	private int priority;

	@Bean(name = JmsTemplateNames.GENERATEOPTIONS_JMS_MESSAGE_TEMPLATE)
	public JmsTemplate generatуOptionsJmsMessageTemplate(ConnectionFactory connectionFactory)
	{
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(MessageQueuesName.GENERATEOPTIONS_MESSAGE_QUEUE);
		jmsTemplate.setExplicitQosEnabled(true);
		jmsTemplate.setPriority(priority);
		return jmsTemplate;
	}
}
