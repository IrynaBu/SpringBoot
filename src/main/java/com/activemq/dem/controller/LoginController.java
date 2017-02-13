package com.activemq.dem.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequestMapping("/login")
public class LoginController
{
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, produces={"application/json"})
	public String login()
	{
		log.info("In hello");
		return "Hello";
	}
}
