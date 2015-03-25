package com.yidian_erhuo.entity;

import java.util.ArrayList;

public class EntityGoodsSearch extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String id = "";
	private String title = "";
	private String message = "";
	private String price = "";
	private String collectNum = "";
	private String commentNum = "";
	private String publishTimeTemp = "";
	private int collect = 0;
	private int total = 0;
	private ArrayList<EntityImage> entityImages = new ArrayList<EntityImage>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<EntityImage> getEntityImages() {
		return entityImages;
	}

	public void setEntityImages(ArrayList<EntityImage> entityImages) {
		this.entityImages = entityImages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public String getPublishTimeTemp() {
		return publishTimeTemp;
	}

	public void setPublishTimeTemp(String publishTimeTemp) {
		this.publishTimeTemp = publishTimeTemp;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}

}