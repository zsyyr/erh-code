package com.yidian_erhuo.entity;

import com.easemob.chat.EMMessage;

public class EntityChat extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String userId = "";
	private String header = "";
	private String message = "";
	private String name = "";

	private boolean isHistory = false;
	private boolean isMe = false;

	private EMMessage emMessage;

	public boolean isHistory() {
		return isHistory;
	}

	public void setHistory(boolean isHistory) {
		this.isHistory = isHistory;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EMMessage getEmMessage() {
		return emMessage;
	}

	public void setEmMessage(EMMessage emMessage) {
		this.emMessage = emMessage;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isMe() {
		return isMe;
	}

	public void setMe(boolean isMe) {
		this.isMe = isMe;
	}

}