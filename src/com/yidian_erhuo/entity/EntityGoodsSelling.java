package com.yidian_erhuo.entity;

import java.util.ArrayList;

public class EntityGoodsSelling extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String id = "";
	private String cid = "";
	private String title = "";
	private String message = "";
	private String price = "";
	private String originPrice = "";
	private String publishTimeTemp = "";
	private String collectNum = "";
	private String commentNum = "";
	private String userId = "";
	private String userHeader = "";
	private String userName = "";
	private String userNickName = "";
	private int collect = 0;
	private int total = 0;
	private ArrayList<EntityImage> entityImages = new ArrayList<EntityImage>();

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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

	public String getPublishTimeTemp() {
		return publishTimeTemp;
	}

	public void setPublishTimeTemp(String publishTimeTemp) {
		this.publishTimeTemp = publishTimeTemp;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserHeader() {
		return userHeader;
	}

	public void setUserHeader(String userHeader) {
		this.userHeader = userHeader;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public int getCollect() {
		return collect;
	}

	public void setCollect(int collect) {
		this.collect = collect;
	}

	public ArrayList<EntityImage> getEntityImages() {
		return entityImages;
	}

	public void setEntityImages(ArrayList<EntityImage> entityImages) {
		this.entityImages = entityImages;
	}
}
