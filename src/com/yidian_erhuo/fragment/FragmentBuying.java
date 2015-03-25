package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.widget.ListView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterBuying;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityGoodsBuying;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.pullToRefresh.PullToRefreshListView;
import com.yidian_erhuo.utile.FrameLoading;

public class FragmentBuying extends FragmentBase implements
		OnRefreshListener<ListView> {
	public final static int PUBLISH_OK = 11;
	private String pageIndex = "1", displayNumber = "15";

	private ListView listView;
	private FrameLoading frameLoading;
	private AdapterBuying adapterBuying;
	private PullToRefreshListView pullToRefreshListView;

	@Override
	public void initView() {
		setContentView(R.layout.fragment_buy);
		// 加载动画
		frameLoading = new FrameLoading(getActivity(), contentView);
		// 加载控件
		pullToRefreshListView = (PullToRefreshListView) contentView
				.findViewById(R.id.pulltorefresh_buying);
		// 下拉刷新,上拉加载
		adapterBuying = new AdapterBuying(getActivity());
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setOnRefreshListener(this);
		pullToRefreshListView.setPullRefreshEnabled(true);
		listView = pullToRefreshListView.getRefreshableView();
		listView.setSelector(android.R.color.transparent);
		listView.setAdapter(adapterBuying);
		listView.setFadingEdgeLength(0);
		listView.setDividerHeight(0);
		// 开启动画
		frameLoading.showFrame();
		// 加载数据
		new CloudAPI().GoodsBuying(pageIndex, displayNumber, new GoodsBuying());
	}

	@Override
	public void onRefresh() {
		pageIndex = "1";
		displayNumber = "15";
		new CloudAPI().GoodsBuying(pageIndex, displayNumber, new GoodsBuying());
	}

	/**
	 * 求购列表监听
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsBuying implements OnPostRequest<ArrayList<EntityGoodsBuying>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsBuying> temp) {
			if ("1".equals(pageIndex)) {// 如果加载的是第一页
				adapterBuying.setEntityGoodsBuyings(temp);
			} else {
				for (int i = 0; i < temp.size(); i++) {
					adapterBuying.getEntityGoodsBuyings().add(temp.get(i));
				}
			}
			frameLoading.hideFrame();
			adapterBuying.notifyDataSetChanged();
			pullToRefreshListView.onPullUpRefreshComplete();
			pullToRefreshListView.onPullDownRefreshComplete();
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().GoodsBuying(pageIndex, displayNumber,
					new GoodsBuying());
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex = "1";
		new CloudAPI().GoodsBuying(pageIndex, displayNumber, new GoodsBuying());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		int totle = adapterBuying.getEntityGoodsBuyings().get(0).getTotal();
		int index = Integer.parseInt(pageIndex) + 1;
		if (totle >= index) {
			pageIndex = index + "";
			new CloudAPI().GoodsBuying(pageIndex, displayNumber,
					new GoodsBuying());
		} else {
			pullToRefreshListView.onPullUpRefreshComplete();
			Toast.makeText(getActivity(), "数据已加载完成", Toast.LENGTH_SHORT).show();
		}
	}
}