package com.activemq.dem.service.activemq.listeners;

import com.activemq.dem.service.Email;
import com.activemq.dem.service.activemq.config.MessageQueuesName;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Log4j
@Component
public class CenshareMessageListener implements PriorityMessageListener
{
	@Value("${pp.jms.censhare.priority}")
	private int priority;

	@JmsListener(destination = MessageQueuesName.CENSHARE_MESSAGE_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Email email) {
		try
		{
			int randomNum = ThreadLocalRandom.current().nextInt(500, 5_000 + 1);
			Thread.sleep(randomNum);
		}
		catch (InterruptedException e)
		{
			log.error(e);
		}
		log.info(Thread.currentThread().getName());
    }

	@Override
	public int getPriority()
	{
		return priority;
	}
}
