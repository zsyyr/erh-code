package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.widget.ListView;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterUserinfoBuying;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityGoodsBuying;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.pullToRefresh.PullToRefreshListView;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("ClickableViewAccessibility")
public class FragmentUserinfoBuying extends FragmentBase implements
		OnRefreshListener<ListView> {
	private String uid = "";
	private boolean isMe = false;
	private String pageIndex = "1", displayNumber = "15";

	private ListView listview;
	private FrameLoading frameLoading;
	private PullToRefreshListView pullToRefreshListView;
	private AdapterUserinfoBuying adapterUserinfoBuying;

	public FragmentUserinfoBuying(String uid, boolean isMe) {
		this.uid = uid;
		this.isMe = isMe;
	}

	public FragmentUserinfoBuying(boolean isMe) {
		this.isMe = isMe;
	}

	@Override
	public void initView() {
		setContentView(R.layout.fragment_userinfo_list);

		frameLoading = new FrameLoading(getActivity(), contentView);
		pullToRefreshListView = (PullToRefreshListView) contentView
				.findViewById(R.id.pulltorefresh_userinfo);
		adapterUserinfoBuying = new AdapterUserinfoBuying(getActivity(), isMe);
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setOnRefreshListener(this);
		pullToRefreshListView.setPullRefreshEnabled(true);
		listview = pullToRefreshListView.getRefreshableView();
		listview.setSelector(android.R.color.transparent);
		listview.setAdapter(adapterUserinfoBuying);
		listview.setFadingEdgeLength(0);
		listview.setDividerHeight(0);
	}

	@Override
	public void onRefresh() {
		frameLoading.showFrame();
		if (isMe) {
			uid = Utile.getUserInfo(getActivity()).getId();
		}
		new CloudAPI().UserGoodsBuying(uid, pageIndex, displayNumber,
				new GoodsBuying());

	}

	/**
	 * 用户求购列表
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsBuying implements OnPostRequest<ArrayList<EntityGoodsBuying>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsBuying> temp) {
			if ("1".equals(pageIndex)) {
				adapterUserinfoBuying.setEntityGoodsBuyings(temp);
			} else {
				for (int i = 0; i < temp.size(); i++) {
					adapterUserinfoBuying.getEntityGoodsBuyings().add(
							temp.get(i));
				}
			}
			frameLoading.hideFrame();
			adapterUserinfoBuying.notifyDataSetChanged();
			pullToRefreshListView.onPullUpRefreshComplete();
			pullToRefreshListView.onPullDownRefreshComplete();
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().UserGoodsBuying(uid, pageIndex, displayNumber,
					new GoodsBuying());
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex = "1";
		displayNumber = "15";
		new CloudAPI().UserGoodsBuying(uid, pageIndex, displayNumber,
				new GoodsBuying());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		int totle = adapterUserinfoBuying.getEntityGoodsBuyings().get(0)
				.getTotal();
		int index = Integer.parseInt(pageIndex) + 1;
		if (totle >= index) {
			pageIndex = index + "";
			new CloudAPI().UserGoodsBuying(uid, pageIndex, displayNumber,
					new GoodsBuying());
		} else {
			pullToRefreshListView.onPullUpRefreshComplete();
		}
	}
}