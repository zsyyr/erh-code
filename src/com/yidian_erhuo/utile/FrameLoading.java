package com.yidian_erhuo.utile;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidian_erhuo.R;

public class FrameLoading {
	private LinearLayout linearlayout_background;
	private AnimationDrawable animationDrawable;
	private TextView textView;
	private Activity activity;
	private ImageView imageview;

	public FrameLoading(Activity activity) {
		this.activity = activity;
		// 实例化控件
		imageview = (ImageView) activity
				.findViewById(R.id.imageview_frame_loading);
		textView = (TextView) activity
				.findViewById(R.id.textview_frame_loading);
		linearlayout_background = (LinearLayout) activity
				.findViewById(R.id.linearlayout_frame_loading);
		// 获取imageview的动画
		animationDrawable = (AnimationDrawable) imageview.getBackground();
	}

	public FrameLoading(Activity activity, View contentView) {
		this.activity = activity;
		// 实例化控件
		ImageView imageview = (ImageView) contentView
				.findViewById(R.id.imageview_frame_loading);
		textView = (TextView) contentView
				.findViewById(R.id.textview_frame_loading);
		linearlayout_background = (LinearLayout) contentView
				.findViewById(R.id.linearlayout_frame_loading);
		// 获取imageview的动画
		animationDrawable = (AnimationDrawable) imageview.getBackground();
	}

	/**
	 * 设置背景颜色
	 * 
	 * @param color
	 */
	public void setBackground(int color) {
		linearlayout_background.setBackgroundColor(activity.getResources()
				.getColor(color));
	}

	/**
	 * 清空信息
	 */
	public void clearMessage() {
		textView.setText("");
	}

	/**
	 * 设置信息
	 * 
	 * @param str
	 */
	public void setMessage(String str) {
		textView.setText(str);
	}

	/**
	 * 显示动画
	 */
	public void showFrame() {
		animationDrawable.start();
		linearlayout_background.setVisibility(View.VISIBLE);
		if (null != imageview) {
			imageview.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏动画
	 */
	public void hideFrame() {
		textView.setText("");
		linearlayout_background.setVisibility(View.GONE);
	}

	/**
	 * 显示错误信息
	 */
	public void errMessage(String msg) {
		imageview.setVisibility(View.INVISIBLE);
		textView.setText(msg);
	}
}