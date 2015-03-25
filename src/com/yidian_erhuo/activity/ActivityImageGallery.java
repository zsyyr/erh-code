package com.yidian_erhuo.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterViewPager;
import com.yidian_erhuo.entity.EntityImage;
import com.yidian_erhuo.fragment.FragmentBase;
import com.yidian_erhuo.fragment.FragmentImage;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("ClickableViewAccessibility")
@SuppressWarnings("unchecked")
public class ActivityImageGallery extends ActivityBase {
	private int id = 0;

	private ViewPager viewPager;
	private ArrayList<EntityImage> entityImages;

	@Override
	public void initView() {
		setContentView(R.layout.activity_image_gallery);

		// 得到传递过来的数据
		id = getIntent().getIntExtra("id", 0);
		entityImages = (ArrayList<EntityImage>) getIntent()
				.getSerializableExtra("entityImages");
		// 实例化控件
		viewPager = (ViewPager) findViewById(R.id.viewpager_image_gallery);
		// 遍历列表
		ArrayList<FragmentBase> fragmentBases = new ArrayList<FragmentBase>();
		for (int i = 0; i < entityImages.size(); i++) {
			fragmentBases.add(new FragmentImage(Utile.HTTP_IMAGE_URL
					+ entityImages.get(i).getImage(), true));
		}
		// 设置适配器
		viewPager.setAdapter(new AdapterViewPager(getSupportFragmentManager(),
				fragmentBases));
		// 设置当前图片所在位置
		viewPager.setCurrentItem(id);
	}
}