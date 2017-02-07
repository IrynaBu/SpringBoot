package com.activemq.dem.controller;

import com.activemq.dem.service.CheckinMessageService;
import com.activemq.dem.service.Email;
import com.activemq.dem.service.FrozenMessageService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequestMapping("/frozenmessages")
public class FrozenMessageController
{
	@Autowired
	private FrozenMessageService frozenMessageService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	public void sendMessage()
	{
		Email email = new Email();
		email.setTo("Frozen");
		email.setBody("frozen message");
		frozenMessageService.sendEmail(email);
	}
}
