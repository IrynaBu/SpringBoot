package service;

import com.activemq.dem.configuration.WebAppInitializer;
import com.activemq.dem.controller.config.ControllerAdwice;
import com.activemq.dem.dao.config.DaoConfig;
import com.activemq.dem.service.Email;
import com.activemq.dem.service.activemq.config.JmsConfig;
import com.activemq.dem.service.config.BusinessConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
	WebAppInitializer.class,
	DaoConfig.class,
	BusinessConfig.class,
	ControllerAdwice.class,
	JmsConfig.class})
public class WebAppInitializerTests
{
	@Autowired
	private JmsTemplate jmsTemplate;

	@Test
	public void contextLoads()
	{
		Email email = new Email();
		email.setTo("Jagi");
		email.setBody("Hello!!!");
		jmsTemplate.convertAndSend("ActiveMQ.Advisory.TempTopic", email);
	}

	@JmsListener(destination = "ActiveMQ.Advisory.TempTopic")
	public void receiveMessage(Email email) {
		System.out.println("Received <" + email + ">");
		//log.info("Received <" + email + ">");
	}
}
