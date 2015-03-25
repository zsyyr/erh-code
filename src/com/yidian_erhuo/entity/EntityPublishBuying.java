package com.yidian_erhuo.entity;

public class EntityPublishBuying extends EntityBase {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String sid = "";
	private String price = "";
	private String subject = "";
	private String message = "";

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}