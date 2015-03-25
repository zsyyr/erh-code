package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityPersonalCenter;
import com.yidian_erhuo.adapter.AdapterViewPager;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.Utile;

public class FragmentUserInfo extends FragmentBase implements
		OnPageChangeListener {
	private ViewPager viewPager;
	private ImageView imageView_sex;
	private TextView textView_username;
	private ImageButton imageButton_heading;
	private ArrayList<FragmentBase> fragmentBases;
	private DisplayImageOptions displayImageOptions;
	private ArrayList<TextView> buttonTexts = new ArrayList<TextView>();
	private ArrayList<ImageView> buttonImages = new ArrayList<ImageView>();
	private ArrayList<LinearLayout> buttons = new ArrayList<LinearLayout>();

	@Override
	public void initView() {
		setContentView(R.layout.fragment_userinfo);
		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		// 控件初始化
		imageView_sex = (ImageView) contentView
				.findViewById(R.id.imageview_userinfo_sex);
		viewPager = (ViewPager) contentView
				.findViewById(R.id.viewpager_userinfo);
		textView_username = (TextView) contentView
				.findViewById(R.id.textview_userinfo_username);
		imageButton_heading = (ImageButton) contentView
				.findViewById(R.id.imagebutton_userinfo_head);
		viewPager.setOnPageChangeListener(this);
		// 用户头像点击事件
		imageButton_heading.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getActivity(),
						ActivityPersonalCenter.class), 0);
			}
		});
		// 加载四个按钮以及图标和文字
		buttons.add((LinearLayout) contentView
				.findViewById(R.id.linearlayout_userinfo_1));
		buttons.add((LinearLayout) contentView
				.findViewById(R.id.linearlayout_userinfo_2));
		buttons.add((LinearLayout) contentView
				.findViewById(R.id.linearlayout_userinfo_3));
		buttons.add((LinearLayout) contentView
				.findViewById(R.id.linearlayout_userinfo_4));
		buttonImages.add((ImageView) contentView
				.findViewById(R.id.imageview_userinfo_1));
		buttonImages.add((ImageView) contentView
				.findViewById(R.id.imageview_userinfo_2));
		buttonImages.add((ImageView) contentView
				.findViewById(R.id.imageview_userinfo_3));
		buttonImages.add((ImageView) contentView
				.findViewById(R.id.imageview_userinfo_4));
		buttonTexts.add((TextView) contentView
				.findViewById(R.id.textview_userinfo_1));
		buttonTexts.add((TextView) contentView
				.findViewById(R.id.textview_userinfo_2));
		buttonTexts.add((TextView) contentView
				.findViewById(R.id.textview_userinfo_3));
		buttonTexts.add((TextView) contentView
				.findViewById(R.id.textview_userinfo_4));
		// 四个按钮的点击事件
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
		// 默认选择第一位
		selectButton(0);
	}

	@Override
	public void onRefresh() {
		EntityUserInfo entityUserInfo = Utile.getUserInfo(getActivity());
		if (null == fragmentBases) {
			fragmentBases = new ArrayList<FragmentBase>();
			fragmentBases.add(new FragmentUserinfoSelling(true));
			fragmentBases.add(new FragmentUserinfoBuying(true));
			fragmentBases.add(new FragmentUserinfoCollect());
			fragmentBases.add(new FragmentUserinfoComment());
			viewPager.setAdapter(new AdapterViewPager(getFragmentManager(),
					fragmentBases));
		}
		// 加载用户头像
		String imageUrl = Utile.HTTP_IMAGE_URL + entityUserInfo.getHeader();
		ImageLoader.getInstance().displayImage(imageUrl, imageButton_heading,
				displayImageOptions);
		// 加载其他数据
		if ("".equals(entityUserInfo.getNickName())) {
			textView_username.setText(entityUserInfo.getName());
		} else {
			textView_username.setText(entityUserInfo.getNickName());
		}
		if ("1".equals(entityUserInfo.getSex())) {
			imageView_sex.setImageResource(R.drawable.icon_sex_boy);
		} else {
			imageView_sex.setImageResource(R.drawable.icon_sex_girl);
		}
		fragmentBases.get(viewPager.getCurrentItem()).onRefresh();
	}

	private void selectButton(int id) {
		for (int i = 0; i < buttons.size(); i++) {
			if (id == i) {
				buttons.get(i).setEnabled(false);
				buttonImages.get(i).setEnabled(false);
				buttonTexts.get(i).setTextColor(
						getActivity().getResources()
								.getColor(R.color.text_pink));
			} else {
				buttons.get(i).setEnabled(true);
				buttonImages.get(i).setEnabled(true);
				buttonTexts.get(i).setTextColor(
						getActivity().getResources().getColor(
								R.color.text_black));
			}
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
		fragmentBases.get(arg0).onRefresh();
		selectButton(arg0);
	}
}