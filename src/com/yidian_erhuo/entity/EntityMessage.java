package com.yidian_erhuo.entity;

public class EntityMessage extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String friendId = "";
	private String lastMessage = "";
	private String friendHeader = "";
	private String friendNickName = "";
	private String lastMessageTimeTemp = "";


	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	
	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public String getFriendHeader() {
		return friendHeader;
	}

	public void setFriendHeader(String friendHeader) {
		this.friendHeader = friendHeader;
	}

	public String getFriendNickName() {
		return friendNickName;
	}

	public void setFriendNickName(String friendNickName) {
		this.friendNickName = friendNickName;
	}

	public String getLastMessageTimeTemp() {
		return lastMessageTimeTemp;
	}

	public void setLastMessageTimeTemp(String lastMessageTimeTemp) {
		this.lastMessageTimeTemp = lastMessageTimeTemp;
	}

}