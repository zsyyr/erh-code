package com.yidian_erhuo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterViewPager;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.fragment.FragmentBase;
import com.yidian_erhuo.fragment.FragmentUserinfoBuying;
import com.yidian_erhuo.fragment.FragmentUserinfoSelling;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

public class ActivityUserInfo extends ActivityBase implements
		OnPageChangeListener {
	private String uid = "";

	private ViewPager viewPager;
	private ImageView imageView_sex;
	private FrameLoading frameLoading;
	private TextView textView_username;
	private ImageButton imageButton_head;
	private EntityUserInfo entityUserInfo;
	private Button button_chat, button_back;
	private DisplayImageOptions displayImageOptions;
	private ArrayList<TextView> buttonTexts = new ArrayList<TextView>();
	private ArrayList<ImageView> buttonImages = new ArrayList<ImageView>();
	private ArrayList<LinearLayout> buttons = new ArrayList<LinearLayout>();
	private ArrayList<FragmentBase> fragmentBases = new ArrayList<FragmentBase>();

	@Override
	public void initView() {
		setContentView(R.layout.activity_userinfo);

		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		// 得到用户id
		uid = getIntent().getStringExtra("uid");
		frameLoading = new FrameLoading(this);
		button_back = (Button) findViewById(R.id.button_userinfo_back);
		viewPager = (ViewPager) findViewById(R.id.viewpager_userinfo);
		button_chat = (Button) findViewById(R.id.button_userinfo_chat);
		imageView_sex = (ImageView) findViewById(R.id.imageview_userinfo_sex);
		imageButton_head = (ImageButton) findViewById(R.id.imagebutton_userinfo_head);
		textView_username = (TextView) findViewById(R.id.textview_userinfo_username);

		buttons.add((LinearLayout) findViewById(R.id.linearlayout_userinfo_1));
		buttons.add((LinearLayout) findViewById(R.id.linearlayout_userinfo_2));
		buttonImages.add((ImageView) findViewById(R.id.imageview_userinfo_1));
		buttonImages.add((ImageView) findViewById(R.id.imageview_userinfo_2));
		buttonTexts.add((TextView) findViewById(R.id.textview_userinfo_1));
		buttonTexts.add((TextView) findViewById(R.id.textview_userinfo_2));
		button_back.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);

		for (int i = 0; i < buttons.size(); i++) {
			final int j = i;
			buttons.get(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					selectButton(j);
					viewPager.setCurrentItem(j);
				}
			});
		}

		button_chat.setOnClickListener(this);
		// 加载动画
		frameLoading.showFrame();
		// 加载数据
		new CloudAPI().UserInfo(uid, new UserInfo());
		// 加载分页
		fragmentBases.add(new FragmentUserinfoSelling(uid, false));
		fragmentBases.add(new FragmentUserinfoBuying(uid, false));
		viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(),
				fragmentBases));
		selectButton(0);
		fragmentBases.get(0).onRefresh();
	}

	/**
	 * 获取用户信息
	 * 
	 * @author Administrator
	 * 
	 */
	class UserInfo implements OnPostRequest<EntityUserInfo> {

		@Override
		public void onPostOk(EntityUserInfo temp) {
			entityUserInfo = temp;
			// 填充用户数据
			String imageUrl = Utile.HTTP_IMAGE_URL + temp.getHeader();
			ImageLoader.getInstance().displayImage(imageUrl, imageButton_head,
					displayImageOptions);
			textView_username.setText(temp.getName());
			if ("1".equals(temp.getSex())) {
				imageView_sex.setImageResource(R.drawable.icon_sex_boy);
			} else {
				imageView_sex.setImageResource(R.drawable.icon_sex_girl);
			}
			frameLoading.hideFrame();
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().UserInfo(uid, new UserInfo());
		}
	}

	private void selectButton(int id) {
		for (int i = 0; i < buttons.size(); i++) {
			if (id == i) {
				buttons.get(i).setEnabled(false);
				buttonImages.get(i).setEnabled(false);
				buttonTexts.get(i).setTextColor(
						getResources().getColor(R.color.text_pink));
			} else {
				buttons.get(i).setEnabled(true);
				buttonImages.get(i).setEnabled(true);
				buttonTexts.get(i).setTextColor(
						getResources().getColor(R.color.text_black));
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_userinfo_back:
			finish();
			break;
		case R.id.button_userinfo_chat:
			if (null != Utile.getUserInfo(ActivityUserInfo.this)) {
				Intent intent = new Intent(ActivityUserInfo.this,
						ActivityChat.class);
				intent.putExtra("friendinfo", entityUserInfo);
				startActivity(intent);
			} else {
				startActivity(new Intent(ActivityUserInfo.this,
						ActivityLogin.class));
			}
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		selectButton(arg0);
		fragmentBases.get(arg0).onRefresh();
	}
}