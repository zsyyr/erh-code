package com.yidian_erhuo.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterClassify;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityGoodsSelling;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.pullToRefresh.PullToRefreshListView;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

public class ActivityClassify extends ActivityBase implements
		OnRefreshListener<ListView> {
	private String str_classify_name = "", str_classify_id = "",
			str_userId = "", pageIndex = "1", displayNumber = "15";

	private ListView listView;
	private TitleView titleView;
	private FrameLoading frameLoading;
	private AdapterClassify adapterClassify;
	private PullToRefreshListView pullToRefreshListView;

	@Override
	public void initView() {
		setContentView(R.layout.activity_classify);

		str_classify_id = getIntent().getStringExtra("classifyId");
		str_classify_name = getIntent().getStringExtra("classifyName");

		titleView = new TitleView(this);
		frameLoading = new FrameLoading(this);
		titleView.MidTextView(str_classify_name, 0);
		titleView.RightButton(R.drawable.icon_search, 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);

		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_classify);
		adapterClassify = new AdapterClassify(this);
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setOnRefreshListener(this);
		pullToRefreshListView.setPullRefreshEnabled(true);
		listView = pullToRefreshListView.getRefreshableView();
		listView.setSelector(android.R.color.transparent);
		listView.setAdapter(adapterClassify);
		listView.setFadingEdgeLength(0);
		listView.setDividerHeight(0);

		titleView.getImageButton_left().setOnClickListener(this);
		titleView.getImageButton_right().setOnClickListener(this);
		// 开启动画
		frameLoading.showFrame();
		// 获取用户id
		if (null != Utile.getUserInfo(this)) {
			str_userId = Utile.getUserInfo(this).getId();
		}
		// 加载数据
		new CloudAPI().GoodsSelling(str_classify_id, str_userId, pageIndex,
				displayNumber, new GoodsSelling());
	}

	/**
	 * 商品列表监听
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsSelling implements OnPostRequest<ArrayList<EntityGoodsSelling>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsSelling> temp) {
			if ("1".equals(pageIndex)) {
				adapterClassify.setEntityGoodsSellings(temp);
			} else {
				for (int i = 0; i < temp.size(); i++) {
					adapterClassify.getEntityGoodsSellings().add(temp.get(i));
				}
			}
			frameLoading.hideFrame();
			adapterClassify.notifyDataSetChanged();
			pullToRefreshListView.onPullUpRefreshComplete();
			pullToRefreshListView.onPullDownRefreshComplete();
		}

		@Override
		public void onPostFailure(String err) {
			// 加载数据
			new CloudAPI().GoodsSelling(str_classify_id, str_userId, pageIndex,
					displayNumber, new GoodsSelling());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.imagebutton_title_right:
			startActivity(new Intent(ActivityClassify.this,
					ActivitySearch.class));
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex = "1";
		new CloudAPI().GoodsSelling(str_classify_id, str_userId, pageIndex,
				displayNumber, new GoodsSelling());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		int totle = adapterClassify.getEntityGoodsSellings().get(0).getTotal();
		int index = Integer.parseInt(pageIndex) + 1;
		if (totle >= index) {
			pageIndex = index + "";
			new CloudAPI().GoodsSelling(str_classify_id, str_userId, pageIndex,
					displayNumber, new GoodsSelling());
		} else {
			pullToRefreshListView.onPullUpRefreshComplete();
			Toast.makeText(ActivityClassify.this, "数据已加载完成", Toast.LENGTH_SHORT)
					.show();
		}
	}
}