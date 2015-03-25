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
		// �ж��Ƿ���Ҫ��������ҳ
		SharedPreferences sharedPreferences = getSharedPreferences("erhuo",
				Activity.MODE_PRIVATE);
		if (sharedPreferences.getBoolean("First", true)) {
			startActivityForResult(new Intent(ActivityMain.this,
					ActivityGuide.class), 0);
		} else {
			startActivity(new Intent(ActivityMain.this, ActivityLoading.class));
		}
		// ��ʼ��������
		titleView = new TitleView(this);
		// ��ʼ���ռ�
		viewPager = (JTViewPager) findViewById(R.id.viewpager_main);
		// ��ʼ���ײ���ť
		initBottom();
		selectButton(0);
		// ��Ҫ����ť����¼�
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
	 * ��ʼ���ײ���ť
	 */
	private void initBottom() {
		// viewpager
		fragmentBases.add(new FragmentHomePage());
		fragmentBases.add(new FragmentBuying());
		fragmentBases.add(new FragmentMessage());
		fragmentBases.add(new FragmentUserInfo());
		viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(),
				fragmentBases));
		// �ײ���ť
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_1));
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_2));
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_3));
		boards.add((LinearLayout) findViewById(R.id.linearlayout_main_board_4));
		// �ײ���ťͼƬ
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_1));
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_2));
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_3));
		boards_images
				.add((ImageView) findViewById(R.id.imageview_main_board_4));
		// �ײ���ť����
		boards_text.add((TextView) findViewById(R.id.textview_main_board_1));
		boards_text.add((TextView) findViewById(R.id.textview_main_board_2));
		boards_text.add((TextView) findViewById(R.id.textview_main_board_3));
		boards_text.add((TextView) findViewById(R.id.textview_main_board_4));
		// ��ť����¼�
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
	 * �ײ���ť��ҳ
	 */
	private void selectButton(int id) {
		// �û�δ��¼״̬�²��ܵ����������ť
		if (id > 1 && null == Utile.getUserInfo(ActivityMain.this)) {
			startActivityForResult(new Intent(ActivityMain.this,
					ActivityLogin.class), 0);
		} else {
			// ��ת��ҳ��
			viewPager.setCurrentItem(id);
			// ˢ�µ�ҳ����
			fragmentBases.get(id).onRefresh();
			// �л���ťЧ��
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
			// �л��������Լ�ˢ�¶���
			switch (id) {
			case 0:
				titleView.show();
				titleView.MidTextView("����", 0);
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
				titleView.MidTextView("��", 0);
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
				titleView.MidTextView("��Ϣ�б�", 0);
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
			Toast.makeText(this, "�ٴε���˳�����", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // ȡ���˳�
				}
			}, 2000);
		} else {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == ActivityLogin.LOGIN_LOGOUT) {// ��¼�ɹ�/ע���ɹ�
			selectButton(0);
		} else if (arg1 == ActivityPersonalCenter.EDIT_DATA) {// �û���Ϣ�޸ĳɹ�
			fragmentBases.get(3).onRefresh();// ˢ���û�ҳ��
		} else if (arg1 == FragmentBuying.PUBLISH_OK) {// ˢ����ҳ��
			fragmentBases.get(1).onRefresh();
		} else if (arg1 == 15) {
			finish();
		}
	}
}