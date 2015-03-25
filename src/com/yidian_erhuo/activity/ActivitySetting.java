package com.yidian_erhuo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.yidian_erhuo.R;
import com.yidian_erhuo.utile.SharePopWindow;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("CommitPrefEdits")
public class ActivitySetting extends ActivityBase {
	private Editor editor;
	private TitleView titleView;
	private Button button_switch;
	private SharePopWindow sharePopWindow;
	private SharedPreferences sharedPreferences;
	private LinearLayout linearLayout_about, linearLayout_help,
			linearLayout_share;

	@Override
	public void initView() {
		setContentView(R.layout.activity_setting);

		sharedPreferences = getSharedPreferences("erhuo", Activity.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		titleView = new TitleView(this);
		titleView.MidTextView("设置", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);

		button_switch = (Button) findViewById(R.id.button_setting_switch);
		linearLayout_about = (LinearLayout) findViewById(R.id.linearlayout_setting_about);
		linearLayout_help = (LinearLayout) findViewById(R.id.linearlayout_setting_help);
		linearLayout_share = (LinearLayout) findViewById(R.id.linearlayout_setting_share);

		button_switch.setOnClickListener(this);
		linearLayout_about.setOnClickListener(this);
		linearLayout_help.setOnClickListener(this);
		linearLayout_share.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);

		// 推送状态开关
		if (sharedPreferences.getBoolean("toast", true)) {
			button_switch.setBackgroundResource(R.drawable.icon_switch_open);
		} else {
			button_switch.setBackgroundResource(R.drawable.icon_switch_close);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.linearlayout_setting_about:
			startActivity(new Intent(ActivitySetting.this, ActivityAbout.class));
			break;
		case R.id.linearlayout_setting_help:
			startActivity(new Intent(ActivitySetting.this, ActivityGuide.class));
			break;
		case R.id.linearlayout_setting_share:
			if (sharePopWindow == null) {
				String shareMessage = "在校大学生二手物品交易平台App 体验一下“贰货”"
						+ Utile.THIRD_SHARE_URL;
				sharePopWindow = new SharePopWindow(ActivitySetting.this,
						shareMessage);
			}
			sharePopWindow.showAtLocation(
					findViewById(R.id.linearlayout_setting), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.button_setting_switch:
			if ("0".equals(button_switch.getTag())) {
				editor.putBoolean("toast", false);
				editor.commit();
				button_switch.setTag("1");
				button_switch
						.setBackgroundResource(R.drawable.icon_switch_close);
			} else {
				editor.putBoolean("toast", true);
				editor.commit();
				button_switch.setTag("0");
				button_switch
						.setBackgroundResource(R.drawable.icon_switch_open);
			}
			break;
		case R.id.imagebutton_share_qq_chat:

			break;
		case R.id.imagebutton_share_qq_friend:

			break;
		case R.id.imagebutton_share_qq_space:

			break;
		case R.id.imagebutton_share_qq_weibo:

			break;
		case R.id.imagebutton_share_renren:

			break;
		case R.id.imagebutton_share_sina:

			break;
		}
	}
}