package com.activemq.dem.service.config;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Log4j
@Configuration
@ComponentScan({"com.activemq.dem.service"})
public class BusinessConfig
{
}
