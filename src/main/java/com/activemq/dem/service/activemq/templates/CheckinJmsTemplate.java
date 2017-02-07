package com.activemq.dem.service.activemq.templates;

import com.activemq.dem.service.activemq.config.JmsTemplateNames;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

@Log4j
@Component
public class CheckinJmsTemplate
{
	@Bean(name = JmsTemplateNames.CHECKIN_JMS_MESSAGE_TEMPLATE)
	public JmsTemplate checkinJmsMessageTemplate(ConnectionFactory connectionFactory)
	{
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setDefaultDestinationName(MessageQueuesName.CHECKIN_MESSAGE_QUEUE);
		jmsTemplate.setExplicitQosEnabled(true);
		jmsTemplate.setPriority(7);
		return jmsTemplate;
	}
}
