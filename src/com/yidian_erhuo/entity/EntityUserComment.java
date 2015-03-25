package com.yidian_erhuo.entity;

import java.util.ArrayList;

public class EntityUserComment extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String message = "";
	private String timeTemp = "";
	private String sid = "";
	private String cid = "";
	private String title = "";
	private ArrayList<EntityImage> entityImages = new ArrayList<EntityImage>();

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimeTemp() {
		return timeTemp;
	}

	public void setTimeTemp(String timeTemp) {
		this.timeTemp = timeTemp;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<EntityImage> getEntityImages() {
		return entityImages;
	}

	public void setEntityImages(ArrayList<EntityImage> entityImages) {
		this.entityImages = entityImages;
	}

}