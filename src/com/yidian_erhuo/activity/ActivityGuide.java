package com.yidian_erhuo.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.yidian_erhuo.R;

public class ActivityGuide extends ActivityBase implements OnPageChangeListener {
	private ViewPager viewPager;
	private ArrayList<View> views = new ArrayList<View>();
	private int[] images = { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };

	@Override
	public void initView() {
		setContentView(R.layout.activity_guide);

		for (int i = 0; i < images.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageResource(images[i]);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			views.add(imageView);
		}
		views.add(new View(this));

		viewPager = (ViewPager) findViewById(R.id.viewpager_guide);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(this);
	}

	/**
	 * ViewPagerÊÊÅäÆ÷
	 */
	public class MyPagerAdapter extends PagerAdapter {
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int id) {
		if (views.size() - 1 == id) {
			SharedPreferences sharedPreferences = getSharedPreferences("erhuo",
					Activity.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("First", false);
			editor.commit();
			finish();
		}
	}

	@Override
	public void KeyBack() {
		setResult(15);
		finish();
	}
}