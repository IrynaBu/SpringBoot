package com.activemq.dem.service.impl;

import com.activemq.dem.service.Email;
import com.activemq.dem.service.CheckinMessageService;
import com.activemq.dem.service.activemq.config.JmsTemplateNames;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class CheckinMessageServiceImpl implements CheckinMessageService
{
	@Autowired
	@Qualifier(value = JmsTemplateNames.CHECKIN_JMS_MESSAGE_TEMPLATE)
	private JmsTemplate checkinJmsTemplate;

	@Override
	public void sendEmail(Email email)
	{
		for (int i = 0; i < 1000; i++)
		{
			//log.info("Send message to checkin queue");
			checkinJmsTemplate.convertAndSend(MessageQueuesName.CHECKIN_MESSAGE_QUEUE, email);
		}
	}
}
