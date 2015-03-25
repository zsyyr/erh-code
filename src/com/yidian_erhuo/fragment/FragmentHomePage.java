package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityClassify;
import com.yidian_erhuo.adapter.AdapterGrideClassify;
import com.yidian_erhuo.adapter.AdapterViewPager;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityGoodsClassify;
import com.yidian_erhuo.entity.EntityGoodsFocus;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

public class FragmentHomePage extends FragmentBase implements
		OnPageChangeListener, OnItemClickListener {
	private boolean isLoad = false;
	private String pageIndex = "1", displayNumber = "15";

	private GridView gridView;
	private ViewPager viewPager;
	private FrameLoading frameLoading;
	private LinearLayout linearLayout_points;
	private TextView textView_subject, textView_money;
	private ArrayList<EntityGoodsFocus> entityGoodsFocus;
	private ArrayList<EntityGoodsClassify> entityGoodsClassifies;
	private ArrayList<ImageView> points = new ArrayList<ImageView>();
	ArrayList<FragmentBase> fragmentBases = new ArrayList<FragmentBase>();

	@Override
	public void initView() {
		// ����ͼ
		setContentView(R.layout.fragment_home_page);
		// ���ض���
		frameLoading = new FrameLoading(getActivity(), contentView);
		// ʵ�����ؼ�
		viewPager = (ViewPager) contentView
				.findViewById(R.id.viewpager_home_page);
		textView_money = (TextView) contentView
				.findViewById(R.id.textview_home_page_money);
		textView_subject = (TextView) contentView
				.findViewById(R.id.textview_home_page_subject);
		linearLayout_points = (LinearLayout) contentView
				.findViewById(R.id.linearlayout_home_page_points);
		gridView = (GridView) contentView.findViewById(R.id.gridview_home_page);
		// 2��1�ı�������viewpager
		viewPager.post(new Runnable() {
			public void run() {
				int ScreenWidth = getActivity().getWindowManager()
						.getDefaultDisplay().getWidth();
				int Height = ScreenWidth / 3 * 2;
				LayoutParams layoutParams = viewPager.getLayoutParams();
				layoutParams.width = ScreenWidth;
				layoutParams.height = Height;
				viewPager.setLayoutParams(layoutParams);
			}
		});
		// �¼�����
		gridView.setOnItemClickListener(this);
		viewPager.setOnPageChangeListener(this);
		// ��ʼ��������
		requestData();
	}

	private void requestData() {
		// �������ض���
		frameLoading.showFrame();
		// �����б�
		new CloudAPI().GoodsClassify(new GoodsClassify());
		// ����ͼ
		new CloudAPI().GoodsFocus(pageIndex, displayNumber, new GoodsFocus());
	}

	/**
	 * ����ͼ�����¼�
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsFocus implements OnPostRequest<ArrayList<EntityGoodsFocus>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsFocus> temp) {
			if (!isLoad) {
				isLoad = true;
			} else {
				// �رն���
				frameLoading.hideFrame();
			}
			entityGoodsFocus = temp;
			// ��������
			for (int i = 0; i < temp.size(); i++) {
				fragmentBases.add(new FragmentImage(Utile.HTTP_IMAGE_URL
						+ temp.get(i).getImage(), temp.get(i).getId()));
			}
			for (int i = 0; i < fragmentBases.size(); i++) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setPadding(0, 0, 10, 0);
				points.add(imageView);
				linearLayout_points.addView(imageView);
			}
			viewPager.setAdapter(new AdapterViewPager(getFragmentManager(),
					fragmentBases));
			pointSelect(0);
			viewPager.setCurrentItem(0);
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().GoodsFocus(pageIndex, displayNumber,
					new GoodsFocus());
		}
	}

	/**
	 * �����б����
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsClassify implements
			OnPostRequest<ArrayList<EntityGoodsClassify>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsClassify> temp) {
			if (!isLoad) {
				isLoad = true;
			} else {
				// �رն���
				frameLoading.hideFrame();
			}
			entityGoodsClassifies = temp;
			// ������
			gridView.setAdapter(new AdapterGrideClassify(getActivity(), temp));
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().GoodsClassify(new GoodsClassify());
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(final int arg0) {
		pointSelect(arg0);
	}

	private void pointSelect(int id) {
		for (int i = 0; i < points.size(); i++) {
			if (id == i) {
				points.get(i).setImageResource(R.drawable.icon_point_d);
			} else {
				points.get(i).setImageResource(R.drawable.icon_point_u);
			}
		}
		textView_subject.setText(entityGoodsFocus.get(id).getTitle());
		textView_money.setText("��" + entityGoodsFocus.get(id).getPrice());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		Intent intent = new Intent(getActivity(), ActivityClassify.class);
		intent.putExtra("classifyName", entityGoodsClassifies.get(id).getName());
		intent.putExtra("classifyId", entityGoodsClassifies.get(id).getId());
		startActivity(intent);
	}
}