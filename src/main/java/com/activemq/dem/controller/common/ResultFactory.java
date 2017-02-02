package com.activemq.dem.controller.common;

/**
 * Created on 02.07.2015.
 * Project catalog-editor
 */
public class ResultFactory
{
	/**
	 * @param data - data for transmission to the client
	 * @return list values ??to client
	 */
	public static Result getSuccessResult(Object data)
	{
		return new Result(true, data, null, null);
	}

	/**
	 * Send success result creating entity
	 *
	 * @return true to client
	 */
	public static Result getSuccessResult()
	{
		return new Result(true, null, null, null);
	}

	/**
	 * Send success result with message
	 *
	 * @return true to client
	 */
	public static Result getSuccessResult(String msg, String localizationKey)
	{
		return new Result(true, null, msg, localizationKey);
	}

	/**
	 * @param msg - error code
	 * @return Error and error code
	 */
	public static Result getFailResult(String msg, String localizationKey)
	{
		return new Result(false, null, msg, localizationKey);
	}

	public static Result getFailResult(String msg, Object data, String localizationKey)
	{
		return new Result(false, data, msg, localizationKey);
	}
}
