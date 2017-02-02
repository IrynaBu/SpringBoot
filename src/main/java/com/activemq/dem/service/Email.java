package com.activemq.dem.service;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Email implements Serializable
{
	private long id;
	private String to;
	private String body;

	public Email() {
	}

	public Email(String to, String body) {
		this.to = to;
		this.body = body;
	}

	@Override
	public String toString() {
		return String.format("Email{to=%s, body=%s}", getTo(), getBody());
	}
}
