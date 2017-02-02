package com.activemq.dem.controller.common;

import lombok.Getter;

import java.io.Serializable;

/**
 * Created on 02.07.2015.
 * Project catalog-editor
 */
@Getter
public class Result implements Serializable
{
	private static final long serialVersionUID = 8236133065293292690L;
	final private boolean success;
	final private Object data;
	final private String msg;
	final private String localizationKey;

	public Result(boolean success, Object data, String msg, String localizationKey)
	{
		this.data = data;
		this.success = success;
		this.msg = msg;
		this.localizationKey = localizationKey;
	}
}
