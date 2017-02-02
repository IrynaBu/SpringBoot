package com.activemq.dem.configuration;

import com.activemq.dem.controller.config.ControllerAdwice;
import com.activemq.dem.dao.config.DaoConfig;
import com.activemq.dem.service.activemq.config.JmsConfig;
import com.activemq.dem.service.config.BusinessConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@PropertySource(value = "classpath:activemq.properties")
@SpringBootApplication
public class WebAppInitializer extends SpringBootServletInitializer
{
	private static final String MAPPING = "/service/*";
	private static final String SERVLET_NAME = "DispatcherServlet";

	@Autowired
	private WebApplicationContext webApplicationContext;

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(new Class<?>[] {
			WebAppInitializer.class,
			DaoConfig.class,
			BusinessConfig.class,
			ControllerAdwice.class,
			JmsConfig.class}, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
	{
		// errors should be handled via a servlet container and not via spring boot (required for proper work of OAuth)
		setRegisterErrorPageFilter(false);
		return builder.sources(
			new Class<?>[] {WebAppInitializer.class, DaoConfig.class, BusinessConfig.class, ControllerAdwice.class,
				JmsConfig.class});
			//.properties("spring.config.name:application, activemq", "spring.config.location:classpath*:");
	}

	@Bean
	public ServletRegistrationBean dispatcherServlet()
	{
		DispatcherServlet servlet = new DispatcherServlet(webApplicationContext);
		ServletRegistrationBean registration = new ServletRegistrationBean(servlet);
		registration.setName(SERVLET_NAME);
		registration.setAsyncSupported(true);
		registration.setLoadOnStartup(1);
		registration.addUrlMappings(MAPPING);
		return registration;
	}
}
