package com.activemq.dem.service.activemq.listeners;

import com.activemq.dem.service.Email;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import java.util.concurrent.ThreadLocalRandom;

@Log4j
@Component
public class FrozenMessageListener
{
    @JmsListener(destination = MessageQueuesName.FROZEN_MESSAGE_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Email email) {
		try
		{
			int randomNum = ThreadLocalRandom.current().nextInt(500, 10_000 + 1);
			Thread.sleep(randomNum);
		}
		catch (InterruptedException e)
		{
			log.error(e);
		}
		log.info(Thread.currentThread().getName());
    }
}
