package com.yidian_erhuo.entity;

import java.util.ArrayList;

public class EntityUserCollect extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String sid = "";
	private String collectNum = "";
	private ArrayList<EntityImage> entityImages = new ArrayList<EntityImage>();

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	public ArrayList<EntityImage> getEntityImages() {
		return entityImages;
	}

	public void setEntityImages(ArrayList<EntityImage> entityImages) {
		this.entityImages = entityImages;
	}

}