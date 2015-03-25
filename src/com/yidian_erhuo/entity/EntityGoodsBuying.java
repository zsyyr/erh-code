package com.yidian_erhuo.entity;

public class EntityGoodsBuying extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int total = 0;
	private String id = "";
	private String userId = "";
	private String price = "";
	private String title = "";
	private String message = "";
	private String userNickName = "";
	private String userHeader = "";
	private String publishTimeTemp = "";

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
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

	public String getUserHeader() {
		return userHeader;
	}

	public void setUserHeader(String userHeader) {
		this.userHeader = userHeader;
	}

}