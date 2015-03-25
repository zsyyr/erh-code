package com.yidian_erhuo.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterGoodsInfo;
import com.yidian_erhuo.adapter.AdapterViewPager;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsComment;
import com.yidian_erhuo.entity.EntityGoodsInfo;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.fragment.FragmentBase;
import com.yidian_erhuo.fragment.FragmentImage;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.SharePopWindow;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

public class ActivityGoodsInfo extends ActivityBase implements
		OnRefreshListener<ListView>, OnPageChangeListener {
	private String sid = "";
	private boolean isLoad = false;

	private Button button_chat;
	private ViewPager viewPager;
	private TitleView titleView;
	private Button button_submit;
	private EditText editText_input;
	private FrameLoading frameLoading;
	private ImageButton imageButton_head;
	private TextView textView_comment_title;
	private SharePopWindow sharePopWindow;
	private FrameLayout frameLayout_input;
	private InputMethodManager inputMethodManager;
	private DisplayImageOptions displayImageOptions_head;
	private ImageView imageView_collect, imageView_comment;
	private LinearLayout linearLayout_point, linearLayout_comment;
	private TextView textView_price_new, textView_price_old, textView_timetemp,
			textView_subject, textView_message, textView_collect,
			textView_comment;
	private EntityGoodsInfo entityGoodsInfo;
	private AdapterGoodsInfo adapterGoodsInfo;
	private ArrayList<ImageView> points = new ArrayList<ImageView>();

	@Override
	public void initView() {
		setContentView(R.layout.activity_goods_info);

		// 获取商品id
		sid = getIntent().getStringExtra("sid");

		frameLoading = new FrameLoading(this);
		titleView = new TitleView(this);
		titleView.MidTextView("宝贝详情", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		titleView.RightButton(R.drawable.icon_share, 0);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		textView_comment_title = (TextView) findViewById(R.id.textview_goods_info_comment_title);
		button_submit = (Button) findViewById(R.id.button_goods_info_submit);
		editText_input = (EditText) findViewById(R.id.edittext_goods_info_input);
		frameLayout_input = (FrameLayout) findViewById(R.id.framelayout_goods_info_comment);
		button_chat = (Button) findViewById(R.id.button_goods_info_chat);
		imageView_collect = (ImageView) findViewById(R.id.imageview_goods_info_collect);
		imageView_comment = (ImageView) findViewById(R.id.imageview_goods_info_comment);
		textView_collect = (TextView) findViewById(R.id.textview_goods_info_collect);
		textView_comment = (TextView) findViewById(R.id.textview_goods_info_comment);
		viewPager = (ViewPager) findViewById(R.id.viewpager_goods_info);
		linearLayout_point = (LinearLayout) findViewById(R.id.linearlayout_goods_info_point);
		imageButton_head = (ImageButton) findViewById(R.id.imagebutton_goods_info_head);
		textView_message = (TextView) findViewById(R.id.textview_goods_info_message);
		textView_price_new = (TextView) findViewById(R.id.textview_goods_info_price_new);
		textView_price_old = (TextView) findViewById(R.id.textview_goods_info_price_old);
		textView_subject = (TextView) findViewById(R.id.textview_goods_info_subject);
		textView_timetemp = (TextView) findViewById(R.id.textview_goods_info_timetemp);
		linearLayout_comment = (LinearLayout) findViewById(R.id.linearlayout_goods_info_comment);
		adapterGoodsInfo = new AdapterGoodsInfo(this);
		button_chat.setOnClickListener(this);
		viewPager.setOnPageChangeListener(this);
		imageView_collect.setOnClickListener(this);
		imageView_comment.setOnClickListener(this);
		imageButton_head.setOnClickListener(this);
		button_submit.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
		titleView.getImageButton_right().setOnClickListener(this);
		displayImageOptions_head = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		// 1比1缩放viewpager
		viewPager.post(new Runnable() {
			public void run() {
				int ScreenWidth = ActivityGoodsInfo.this.getWindowManager()
						.getDefaultDisplay().getWidth();
				LayoutParams layoutParams = viewPager.getLayoutParams();
				layoutParams.width = ScreenWidth;
				layoutParams.height = ScreenWidth;
				viewPager.setLayoutParams(layoutParams);
			}
		});
		// 删除线
		textView_price_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		// 开启动画
		frameLoading.showFrame();
		// 加载数据
		EntityUserInfo entityUserInfo = Utile
				.getUserInfo(ActivityGoodsInfo.this);
		String uid = "";
		if (null != entityUserInfo) {
			uid = entityUserInfo.getId();
		}
		new CloudAPI().GoodsInfo(sid, uid, new GoodsInfo());
		new CloudAPI().GoodsComment(sid, new GoodsInfoComment());
	}

	/**
	 * 商品详情
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsInfo implements OnPostRequest<EntityGoodsInfo> {

		@Override
		public void onPostOk(EntityGoodsInfo temp) {
			if (!isLoad) {
				isLoad = true;
			} else {
				// 关闭动画
				frameLoading.hideFrame();
			}
			entityGoodsInfo = temp;
			// 加载焦点图
			ArrayList<FragmentBase> fragmentBases = new ArrayList<FragmentBase>();
			for (int i = 0; i < temp.getEntityImages().size(); i++) {
				fragmentBases.add(new FragmentImage(temp.getEntityImages(), i));
			}
			for (int i = 0; i < fragmentBases.size(); i++) {
				ImageView imageView = new ImageView(ActivityGoodsInfo.this);
				imageView.setPadding(0, 0, 10, 0);
				points.add(imageView);
				linearLayout_point.addView(imageView);
			}
			viewPager.setAdapter(new AdapterViewPager(
					getSupportFragmentManager(), fragmentBases));
			pointSelect(0);
			// 加载其他数据
			textView_subject.setText(temp.getTitle());
			textView_message.setText(temp.getMessage());
			textView_collect.setText(temp.getCollectNum());
			textView_comment.setText(temp.getCommentNum());
			textView_price_new.setText("￥" + temp.getPrice());
			textView_price_old.setText("￥" + temp.getOriginalPrice());
			textView_timetemp.setText(Utile.getTime(temp.getPublishTimeTemp()));
			// 判断收藏状态
			imageView_collect.setTag(temp.getCollect() + "");
			if (temp.getCollect() == 0) {
				imageView_collect.setImageResource(R.drawable.icon_collect);
			} else {
				imageView_collect.setImageResource(R.drawable.icon_collect_);
			}
			// 加载用户头像
			String imageUrl = Utile.HTTP_IMAGE_URL + temp.getUserHeader();
			ImageLoader.getInstance().displayImage(imageUrl, imageButton_head,
					displayImageOptions_head);
			// 收藏按钮点击事件
			imageView_collect.setOnClickListener(new GoodsCollect());
		}

		@Override
		public void onPostFailure(String err) {
			EntityUserInfo entityUserInfo = Utile
					.getUserInfo(ActivityGoodsInfo.this);
			String uid = "";
			if (null != entityUserInfo) {
				uid = entityUserInfo.getId();
			}
			new CloudAPI().GoodsInfo(sid, uid, new GoodsInfo());
		}
	}

	/**
	 * 收藏事件
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsCollect implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (null != Utile.getUserInfo(ActivityGoodsInfo.this)) {
				if ("0".equals(imageView_collect.getTag())) {
					Toast.makeText(ActivityGoodsInfo.this, "正在收藏",
							Toast.LENGTH_SHORT).show();
					new CloudAPI().CollectAdd(entityGoodsInfo.getId(), Utile
							.getUserInfo(ActivityGoodsInfo.this).getId(),
							new OnPostRequest<EntityBase>() {
								@Override
								public void onPostOk(EntityBase temp) {
									imageView_collect.setTag("1");
									int count = Integer
											.parseInt(textView_collect
													.getText().toString()) + 1;
									textView_collect.setText(count + "");
									imageView_collect
											.setImageResource(R.drawable.icon_collect_);
								}

								@Override
								public void onPostFailure(String err) {
									Toast.makeText(ActivityGoodsInfo.this,
											"收藏失败，请重试", Toast.LENGTH_SHORT)
											.show();
								}
							});
				} else {
					Toast.makeText(ActivityGoodsInfo.this, "正在取消收藏",
							Toast.LENGTH_SHORT).show();
					new CloudAPI().CollectRemove(entityGoodsInfo.getId(), Utile
							.getUserInfo(ActivityGoodsInfo.this).getId(),
							new OnPostRequest<EntityBase>() {
								@Override
								public void onPostOk(EntityBase temp) {
									imageView_collect.setTag("0");
									int count = Integer
											.parseInt(textView_collect
													.getText().toString()) - 1;
									textView_collect.setText(count + "");
									imageView_collect
											.setImageResource(R.drawable.icon_collect);
								}

								@Override
								public void onPostFailure(String err) {
									Toast.makeText(ActivityGoodsInfo.this,
											"取消收藏失败，请重试", Toast.LENGTH_SHORT)
											.show();
								}
							});
				}
			} else {
				startActivity(new Intent(ActivityGoodsInfo.this,
						ActivityLogin.class));
			}
		}

	}

	/**
	 * 商品评论
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsInfoComment implements
			OnPostRequest<ArrayList<EntityGoodsComment>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsComment> temp) {
			if (!isLoad) {
				isLoad = true;
			} else {
				// 关闭动画
				frameLoading.hideFrame();
			}
			adapterGoodsInfo.setEntityGoodsComments(temp);
			linearLayout_comment.removeAllViews();
			for (int i = 0; i < temp.size(); i++) {
				linearLayout_comment.addView(adapterGoodsInfo.getView(i,
						adapterGoodsInfo.getViewMap().get(i), null));
			}
			((ScrollView) findViewById(R.id.scrollview_goods_info)).scrollTo(0,
					0);
			if (temp.size() == 0) {
				textView_comment_title.setText("暂无评论");
			}
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().GoodsComment(sid, new GoodsInfoComment());
		}
	}

	private void pointSelect(int id) {
		for (int i = 0; i < points.size(); i++) {
			if (id == i) {
				points.get(i).setImageResource(R.drawable.icon_point_d);
			} else {
				points.get(i).setImageResource(R.drawable.icon_point_u);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.imagebutton_title_right:
			if (null == sharePopWindow) {
				String shareMessage = "这个商品不错哟~" + entityGoodsInfo.getTitle()
						+ entityGoodsInfo.getMessage() + Utile.THIRD_SHARE_URL;
				sharePopWindow = new SharePopWindow(ActivityGoodsInfo.this,
						shareMessage);
			}
			sharePopWindow.showAtLocation(
					findViewById(R.id.linearlayout_goods_info), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.imagebutton_goods_info_head:
			EntityUserInfo entityUserInfo = Utile
					.getUserInfo(ActivityGoodsInfo.this);
			if (null == entityUserInfo
					|| (!entityUserInfo.getId().equals(
							entityGoodsInfo.getUserId()))) {
				Intent intent = new Intent(ActivityGoodsInfo.this,
						ActivityUserInfo.class);
				intent.putExtra("uid", entityGoodsInfo.getUserId());
				startActivity(intent);
			}
			break;
		case R.id.button_goods_info_chat:
			if (null == Utile.getUserInfo(ActivityGoodsInfo.this)) {
				startActivity(new Intent(ActivityGoodsInfo.this,
						ActivityLogin.class));
			} else if (!entityGoodsInfo.getUserId().equals(
					Utile.getUserInfo(ActivityGoodsInfo.this).getId())) {
				EntityUserInfo entityUserInfo2 = new EntityUserInfo();
				entityUserInfo2.setId(entityGoodsInfo.getUserId());
				entityUserInfo2.setHeader(entityGoodsInfo.getUserHeader());
				entityUserInfo2.setNickName(entityGoodsInfo.getUserNickName());

				Intent intent = new Intent(ActivityGoodsInfo.this,
						ActivityChat.class);
				intent.putExtra("friendinfo", entityUserInfo2);
				startActivity(intent);
			}
			break;
		case R.id.imageview_goods_info_comment:
			if (null != Utile.getUserInfo(ActivityGoodsInfo.this)) {
				if (frameLayout_input.getVisibility() == View.INVISIBLE) {
					frameLayout_input.setVisibility(View.VISIBLE);
					inputMethodManager.showSoftInput(editText_input, 0);
				}
			} else {
				startActivity(new Intent(ActivityGoodsInfo.this,
						ActivityLogin.class));
			}
			break;
		case R.id.button_goods_info_submit:
			String str_input = editText_input.getText().toString();
			inputMethodManager.hideSoftInputFromWindow(
					editText_input.getWindowToken(), 0);
			if (!"".equals(str_input)) {
				frameLayout_input.setVisibility(View.INVISIBLE);
				new CloudAPI().CommentAdd(sid,
						Utile.getUserInfo(ActivityGoodsInfo.this).getId(),
						str_input, new OnPostRequest<EntityBase>() {
							@Override
							public void onPostOk(EntityBase temp) {
								int count = Integer.parseInt(textView_comment
										.getText().toString());
								count += 1;
								textView_comment.setText(count + "");
								editText_input.setText("");
								new CloudAPI().GoodsComment(sid,
										new GoodsInfoComment());
								textView_comment_title.setText("宝贝评论");
								Toast.makeText(ActivityGoodsInfo.this,
										"评论发表成功", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onPostFailure(String err) {
								Toast.makeText(ActivityGoodsInfo.this,
										"评论发表失败", Toast.LENGTH_SHORT).show();
							}
						});
			} else {
				Toast.makeText(ActivityGoodsInfo.this, "请填写要评论的内容",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int id) {
		pointSelect(id);
	}

	@Override
	public void KeyBack() {
		if (frameLayout_input.getVisibility() == View.VISIBLE) {
			frameLayout_input.setVisibility(View.INVISIBLE);
			inputMethodManager.hideSoftInputFromWindow(
					editText_input.getWindowToken(), 0);
		} else {
			finish();
		}
	}
}