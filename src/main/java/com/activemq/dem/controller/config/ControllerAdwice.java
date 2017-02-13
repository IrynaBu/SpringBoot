package com.activemq.dem.controller.config;

import com.activemq.dem.controller.common.Result;
import com.activemq.dem.controller.common.ResultFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Log4j
@EnableWebMvc
@RestControllerAdvice
@ComponentScan({"com.activemq.dem.controller"})
public class ControllerAdwice implements ResponseBodyAdvice<Object>
{

	@ResponseBody
	@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Data integrity violation")    // 409
	@ExceptionHandler(DataIntegrityViolationException.class)
	public Result conflict(DataIntegrityViolationException e)
	{
		log.error("Request raised a DataIntegrityViolationException", e);
		return ResultFactory.getFailResult("Data integrity violation", null);
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
	{
		return !returnType.getParameterType().isAssignableFrom(Result.class);
	}

	@Override
	public Result beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response)
	{
		if (!(body instanceof Result))
		{
			log.info("Request successfully processed: {}" + request.getURI());
			body = ResultFactory.getSuccessResult(body);
		}
		return (Result)body;
	}
}
