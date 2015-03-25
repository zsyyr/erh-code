package com.yidian_erhuo.entity;

public class EntityGoodsClassify extends EntityBase {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String id = "";
	private String name = "";
	private String icon = "";
	private int iconWidth = 0;
	private int iconHeight = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getIconWidth() {
		return iconWidth;
	}

	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}

	public int getIconHeight() {
		return iconHeight;
	}

	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}

}