package com.yidian_erhuo.entity;

public class EntityGoodsComment extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String id = "";
	private String message = "";
	private String timeTemp = "";
	private String userId = "";
	private String userName = "";
	private String userNickName = "";
	private String userHeader = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUserHeader() {
		return userHeader;
	}

	public void setUserHeader(String userHeader) {
		this.userHeader = userHeader;
	}
}