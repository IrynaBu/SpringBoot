package com.activemq.dem.service.activemq.listeners;

import com.activemq.dem.service.Email;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class FrozenMessageListener
{
    @JmsListener(destination = MessageQueuesName.FROZEN_MESSAGE_QUEUE, concurrency = "4")
    public void receiveMessage(Email email) {
		log.info("Received in frozen container <" + email + ">");
    }

}
