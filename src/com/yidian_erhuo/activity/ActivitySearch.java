package com.yidian_erhuo.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterSearch;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityGoodsSearch;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.pullToRefresh.PullToRefreshListView;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

public class ActivitySearch extends ActivityBase implements
		OnRefreshListener<ListView>, OnItemClickListener {
	private int OrderByPrice = 0;// 默认为时间排序
	private String pageIndex = "1", displayNumber = "15", keyWord = "",
			orderBy = "";

	private ListView listView;
	private EditText editText_input;
	private Button button_order_time;
	private FrameLoading frameLoading;
	private AdapterSearch adapterSearch;
	private LinearLayout linearLayout_order_price;
	private InputMethodManager inputMethodManager;
	private PullToRefreshListView pullToRefreshListView;
	private ImageButton imageButton_back, imageButton_search;
	private ImageView imageView_order_up, imageView_order_down;

	@Override
	public void initView() {
		setContentView(R.layout.activity_search);
		frameLoading = new FrameLoading(this);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		editText_input = (EditText) findViewById(R.id.edittext_search_input);
		button_order_time = (Button) findViewById(R.id.button_search_order_time);
		imageButton_back = (ImageButton) findViewById(R.id.imagebutton_search_back);
		imageButton_search = (ImageButton) findViewById(R.id.imagebutton_search_submit);
		imageView_order_up = (ImageView) findViewById(R.id.imageview_search_order_price_up);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_search);
		imageView_order_down = (ImageView) findViewById(R.id.imageview_search_order_price_down);
		linearLayout_order_price = (LinearLayout) findViewById(R.id.linearlayout_search_order_price);

		adapterSearch = new AdapterSearch(this);
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setOnRefreshListener(this);
		pullToRefreshListView.setPullRefreshEnabled(false);
		listView = pullToRefreshListView.getRefreshableView();
		listView.setSelector(android.R.color.transparent);
		listView.setOnItemClickListener(this);
		listView.setAdapter(adapterSearch);
		listView.setDividerHeight(0);

		imageButton_back.setOnClickListener(this);
		imageButton_search.setOnClickListener(this);
		button_order_time.setOnClickListener(this);
		linearLayout_order_price.setOnClickListener(this);
	}

	/**
	 * 搜索结果回调
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsSearch implements OnPostRequest<ArrayList<EntityGoodsSearch>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsSearch> temp) {
			if (temp.size() == 0) {
				frameLoading.errMessage("无搜索结果");
			} else {
				if ("1".equals(pageIndex)) {
					adapterSearch.setEntityGoodsSearchs(temp);
				} else {
					for (int i = 0; i < temp.size(); i++) {
						adapterSearch.getEntityGoodsSearchs().add(temp.get(i));
					}
				}
				frameLoading.hideFrame();
				adapterSearch.notifyDataSetChanged();
				pullToRefreshListView.onPullUpRefreshComplete();
				pullToRefreshListView.onPullDownRefreshComplete();
			}
		}

		@Override
		public void onPostFailure(String err) {
			EntityUserInfo entityUserInfo = Utile
					.getUserInfo(ActivitySearch.this);
			String uid = "";
			if (null != entityUserInfo) {
				uid = entityUserInfo.getId();
			}
			new CloudAPI().GoodsSearch(keyWord, orderBy, uid, pageIndex,
					displayNumber, new GoodsSearch());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_search_back:
			finish();
			break;
		case R.id.imagebutton_search_submit:
			String str_input = editText_input.getText().toString();
			if ("".equals(str_input)) {
				Toast.makeText(ActivitySearch.this, "请输入要搜索的商品",
						Toast.LENGTH_SHORT).show();
			} else if (!str_input.equals(keyWord)) {
				keyWord = str_input;
				// 加载数据
				frameLoading.showFrame();
				EntityUserInfo entityUserInfo = Utile
						.getUserInfo(ActivitySearch.this);
				String uid = "";
				if (null != entityUserInfo) {
					uid = entityUserInfo.getId();
				}
				new CloudAPI().GoodsSearch(keyWord, orderBy, uid, pageIndex,
						displayNumber, new GoodsSearch());
			}
			// 键盘影藏
			inputMethodManager.hideSoftInputFromWindow(
					editText_input.getWindowToken(), 0);
			break;
		case R.id.button_search_order_time:// 时间排序
			if (0 != adapterSearch.getEntityGoodsSearchs().size()) {
				if (OrderByPrice != 0) {
					OrderByPrice = 0;
					orderBy = "";
					frameLoading.showFrame();
					EntityUserInfo entityUserInfo = Utile
							.getUserInfo(ActivitySearch.this);
					String uid = "";
					if (null != entityUserInfo) {
						uid = entityUserInfo.getId();
					}
					new CloudAPI().GoodsSearch(keyWord, orderBy, uid,
							pageIndex, displayNumber, new GoodsSearch());
				}
				orderByPrice();
			}
			break;
		case R.id.linearlayout_search_order_price:// 价格排序
			if (0 != adapterSearch.getEntityGoodsSearchs().size()) {
				if (OrderByPrice == 0) {
					OrderByPrice = 1;
				} else if (OrderByPrice == 1) {
					OrderByPrice = 2;
				} else if (OrderByPrice == 2) {
					OrderByPrice = 1;
				}
				orderByPrice();
				frameLoading.showFrame();
				EntityUserInfo entityUserInfo = Utile
						.getUserInfo(ActivitySearch.this);
				String uid = "";
				if (null != entityUserInfo) {
					uid = entityUserInfo.getId();
				}
				new CloudAPI().GoodsSearch(keyWord, orderBy, uid, pageIndex,
						displayNumber, new GoodsSearch());
			}
			break;
		}
	}

	private void orderByPrice() {
		switch (OrderByPrice) {
		case 0:// 时间排序,灰
			imageView_order_up
					.setImageResource(R.drawable.icon_arrow_order_up_gray);
			imageView_order_down
					.setImageResource(R.drawable.icon_arrow_order_down_gray);
			break;
		case 1:// 价格排序，从低到高，上粉下灰
			orderBy = "asc";
			imageView_order_up
					.setImageResource(R.drawable.icon_arrow_order_up_pink);
			imageView_order_down
					.setImageResource(R.drawable.icon_arrow_order_down_gray);
			break;
		case 2:// 价格排序,从高到低，上灰下粉
			orderBy = "desc";
			imageView_order_up
					.setImageResource(R.drawable.icon_arrow_order_up_gray);
			imageView_order_down
					.setImageResource(R.drawable.icon_arrow_order_down_pink);
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		int totle = adapterSearch.getEntityGoodsSearchs().get(0).getTotal();
		int index = Integer.parseInt(pageIndex) + 1;
		if (totle >= index) {
			pageIndex = index + "";
			EntityUserInfo entityUserInfo = Utile
					.getUserInfo(ActivitySearch.this);
			String uid = "";
			if (null != entityUserInfo) {
				uid = entityUserInfo.getId();
			}
			new CloudAPI().GoodsSearch(keyWord, orderBy, uid, pageIndex,
					displayNumber, new GoodsSearch());
		} else {
			pullToRefreshListView.onPullUpRefreshComplete();
			Toast.makeText(ActivitySearch.this, "数据已加载完成", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		Intent intent = new Intent(ActivitySearch.this, ActivityGoodsInfo.class);
		intent.putExtra("sid", adapterSearch.getEntityGoodsSearchs().get(id)
				.getId());
		startActivity(intent);
	}
}