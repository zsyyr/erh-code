package com.yidian_erhuo.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterViewPager;
import com.yidian_erhuo.fragment.FragmentBase;
import com.yidian_erhuo.fragment.FragmentBuying;
import com.yidian_erhuo.fragment.FragmentHomePage;
import com.yidian_erhuo.fragment.FragmentMessage;
import com.yidian_erhuo.fragment.FragmentUserInfo;
import com.yidian_erhuo.utile.JTViewPager;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("Recycle")
public class ActivityMain extends ActivityBase {
	private boolean isExit = false;

	private TitleView titleView;
	private JTViewPager viewPager;
	private ArrayList<TextView> boards_text = new ArrayList<TextView>();
	private ArrayList<LinearLayout> boards = new ArrayList<LinearLayout>();
	private ArrayList<ImageView> boards_images = new ArrayList<ImageView>();
	private ArrayList<FragmentBase> fragmentBases = new ArrayList<FragmentBase>();

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		// 判断是否需要进入引导页
		SharedPreferences sharedPreferences = getSharedPreferences("erhuo",
				Activity.MODE_PRIVATE);
		if (sharedPreferences.getBoolean("First", true)) {
			startActivityForResult(new Intent(ActivityMain.this,
					ActivityGuide.class), 0);
		} else {
			startActivity(new Intent(ActivityMain.this, ActivityLoading.class));
		}
		// 初始化标题栏
		titleView = new TitleView(this);
		// 初始化空间
		viewPager = (JTViewPager) findViewById(R.id.viewpager_main);
		// 初始化底部按钮
		initBottom();
		selectButton(0);
		// 我要卖按钮点击事件
		((LinearLayout) findViewById(R.id.linearlayout_main_board_5))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != Utile.getUserInfo(ActivityMain.this)) {
							startActivity(new Intent(ActivityMain.this,
									ActivityPublishSelling.class));
						} else {
							startActivity(new Intent(ActivityMain.this,
									ActivityLogin.class));
						}
					}
				});
	}

	/**
	 * 初始化底部按钮
	 */
	private void initBottom() {
		// viewpager
		fragmentBases.add(new FragmentHomePage());
		fragmentBases.add(new FragmentBuying());
		fragmentBases.add(new FragmentMessage());
		fragmentBases.add(new FragmentUserInfo());
		viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(),
				fragmentBases));
		// 底部按钮
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_1));
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_2));
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_3));
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_4));
		// 底部按钮图片
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_1));
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_2));
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_3));
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_4));
		// 底部按钮文字
		boards_text.add((TextView) findViewById(R.id.textview_main_board_1));
		boards_text.add((TextView) findViewById(R.id.textview_main_board_2));
		boards_text.add((TextView) findViewById(R.id.textview_main_board_3));
		boards_text.add((TextView) findViewById(R.id.textview_main_board_4));
		// 按钮点击事件
		for (int i = 0; i < boards.size(); i++) {
			final int j = i;
			boards.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectButton(j);
				}
			});
		}
	}

	/**
	 * 底部按钮切页
	 */
	private void selectButton(int id) {
		// 用户未登录状态下不能点击后两个按钮
		if (id > 1 && null == Utile.getUserInfo(ActivityMain.this)) {
			startActivityForResult(new Intent(ActivityMain.this,
					ActivityLogin.class), 0);
		} else {
			// 跳转到页面
			viewPager.setCurrentItem(id);
			// 刷新当页数据
			fragmentBases.get(id).onRefresh();
			// 切换按钮效果
			for (int i = 0; i < boards.size(); i++) {
				if (id == i) {
					boards.get(i).setEnabled(false);
					boards_images.get(i).setEnabled(false);
					boards_text.get(i).setTextColor(
							getResources().getColor(R.color.text_pink));
				} else {
					boards.get(i).setEnabled(true);
					boards_images.get(i).setEnabled(true);
					boards_text.get(i).setTextColor(
							getResources().getColor(R.color.text_gray));
				}
			}
			// 切换标题栏以及刷新动作
			switch (id) {
			case 0:
				titleView.show();
				titleView.MidTextView("贰货", 0);
				titleView.LeftButton(R.drawable.icon_setting, 0);
				titleView.RightButton(R.drawable.icon_search, 0);
				titleView.getImageButton_left().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(ActivityMain.this,
										ActivitySetting.class));
							}
						});
				titleView.getImageButton_right().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(ActivityMain.this,
										ActivitySearch.class));
							}
						});
				break;
			case 1:
				titleView.show();
				titleView.MidTextView("求购", 0);
				titleView.RightButton(R.drawable.icon_add, 0);
				titleView.getImageButton_left().setVisibility(View.INVISIBLE);
				titleView.getImageButton_right().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (null != Utile
										.getUserInfo(ActivityMain.this)) {
									startActivityForResult(new Intent(
											ActivityMain.this,
											ActivityPublishBuying.class), 0);
								} else {
									startActivity(new Intent(ActivityMain.this,
											ActivityLogin.class));
								}
							}
						});
				break;
			case 2:
				titleView.show();
				titleView.MidTextView("消息列表", 0);
				titleView.getImageButton_left().setVisibility(View.INVISIBLE);
				titleView.getImageButton_right().setVisibility(View.INVISIBLE);
				break;
			case 3:
				titleView.hide();
				break;
			}
		}
	}

	@Override
	public void KeyBack() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000);
		} else {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == ActivityLogin.LOGIN_LOGOUT) {// 登录成功/注销成功
			selectButton(0);
		} else if (arg1 == ActivityPersonalCenter.EDIT_DATA) {// 用户信息修改成功
			fragmentBases.get(3).onRefresh();// 刷新用户页面
		} else if (arg1 == FragmentBuying.PUBLISH_OK) {// 刷新求购页面
			fragmentBases.get(1).onRefresh();
		} else if (arg1 == 15) {
			finish();
		}
	}
}