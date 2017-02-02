package com.activemq.dem.controller;

import com.activemq.dem.service.Email;
import com.activemq.dem.service.MessageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequestMapping("/messages")
public class MessageController
{
	@Autowired
	private MessageService messageService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public void sendMessage()
	{
		Email email = new Email();
		email.setTo("Jagi");
		email.setBody("Hello!!!");
		log.info("Prepare email and send email");
		messageService.sendEmail(email);
	}
}
