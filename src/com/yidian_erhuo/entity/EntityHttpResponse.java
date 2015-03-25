package com.yidian_erhuo.entity;

import java.io.Serializable;

public class EntityHttpResponse implements Serializable {
	public static final int STATUS_CODE_OK = 200;
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private int StatusCode = 0;
	private String Message = "";

	public int getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(int statusCode) {
		StatusCode = statusCode;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}