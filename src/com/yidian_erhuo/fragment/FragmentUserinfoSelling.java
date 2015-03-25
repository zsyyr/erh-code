package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityGoodsInfo;
import com.yidian_erhuo.activity.ActivityPublishSelling;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsSelling;
import com.yidian_erhuo.entity.EntityImage;
import com.yidian_erhuo.entity.EntityPublishSelling;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.pullToRefresh.PullToRefreshScrollView;
import com.yidian_erhuo.utile.EditPopWindow;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("ClickableViewAccessibility")
public class FragmentUserinfoSelling extends FragmentBase implements
		OnRefreshListener<ScrollView> {
	private String uid = "";
	private int ScreenWidth = 0;
	private boolean isMe = false;
	private String pageIndex = "1", displayNumber = "15";

	private ScrollView scrollView;
	private FrameLoading frameLoading;
	private EditPopWindow editPopWindow;
	private int leftHeight = 0, rightHeight = 0;
	private DisplayImageOptions displayImageOptions;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private ArrayList<EntityGoodsSelling> entityGoodsSellings;
	private LinearLayout linearlayout_left, linearlayout_right;

	public FragmentUserinfoSelling(String uid, boolean isMe) {
		this.uid = uid;
		this.isMe = isMe;
	}

	public FragmentUserinfoSelling(boolean isMe) {
		this.isMe = isMe;
	}

	@Override
	public void initView() {
		setContentView(R.layout.fragment_userinfo_pinterest);
		// 屏幕宽度
		ScreenWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		frameLoading = new FrameLoading(getActivity(), contentView);
		displayImageOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();
		// 控件实例化
		pullToRefreshScrollView = (PullToRefreshScrollView) contentView
				.findViewById(R.id.pulltorefresh_pinterest);
		pullToRefreshScrollView.setPullLoadEnabled(true);
		pullToRefreshScrollView.setOnRefreshListener(this);
		pullToRefreshScrollView.setPullRefreshEnabled(true);
		scrollView = pullToRefreshScrollView.getRefreshableView();
		scrollView.setFadingEdgeLength(0);
		View pinterest = LinearLayout.inflate(getActivity(),
				R.layout.view_pinterest, null);
		linearlayout_left = (LinearLayout) pinterest
				.findViewById(R.id.linearlayout_pinterest_left);
		linearlayout_right = (LinearLayout) pinterest
				.findViewById(R.id.linearlayout_pinterest_right);
		scrollView.addView(pinterest);
		// 加载动画
		frameLoading.showFrame();
	}

	@Override
	public void onRefresh() {
		if (null != frameLoading) {
			frameLoading.showFrame();
		}
		if (isMe) {
			uid = Utile.getUserInfo(getActivity()).getId();
		}
		new CloudAPI().UserGoodsSelling(uid, pageIndex, displayNumber,
				new GoodsSelling());
	}

	/**
	 * 用户售卖列表
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsSelling implements OnPostRequest<ArrayList<EntityGoodsSelling>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsSelling> temp) {
			entityGoodsSellings = temp;
			leftHeight = 0;
			rightHeight = 0;
			linearlayout_left.removeAllViews();
			linearlayout_right.removeAllViews();
			linearlayout_right.setPadding(7, 0, 15, 0);
			linearlayout_left.setPadding(15, 0, 7, 0);
			// 图片显示宽度
			int requireWidth = (ScreenWidth - 44) / 2;
			// 遍历数据
			for (int i = 0; i < temp.size(); i++) {
				if (temp.get(i).getEntityImages().size() > 0) {
					String imageUrl = Utile.HTTP_IMAGE_URL
							+ temp.get(i).getEntityImages().get(0).getImage();
					EntityImage entityImage = temp.get(i).getEntityImages()
							.get(0);
					int imageWidth = entityImage.getImageWidth();
					int imageHeight = entityImage.getImageHeight();

					double radio = requireWidth / (imageWidth * 1.0);
					int requireHeight = (int) (imageHeight * radio);

					if (leftHeight <= rightHeight) {
						leftHeight += requireHeight;
						linearlayout_left.addView(addRow(temp.get(i), imageUrl,
								requireWidth, requireHeight));
					} else {
						rightHeight += requireHeight;
						linearlayout_right.addView(addRow(temp.get(i),
								imageUrl, requireWidth, requireHeight));
					}
				}
			}
			frameLoading.hideFrame();
			pullToRefreshScrollView.onPullUpRefreshComplete();
			pullToRefreshScrollView.onPullDownRefreshComplete();
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().UserGoodsSelling(uid, pageIndex, displayNumber,
					new GoodsSelling());
		}
	}

	private View addRow(final EntityGoodsSelling entityGoodsSelling,
			String imageUrl, int width, int height) {
		View row = LinearLayout.inflate(getActivity(),
				R.layout.view_row_pinterest, null);
		Button button_collect = (Button) row
				.findViewById(R.id.button_row_pinterest_collect);
		ImageView imageView_comment = (ImageView) row
				.findViewById(R.id.imageview_row_pinterest_comment);
		ImageView imageView_background = (ImageView) row
				.findViewById(R.id.imageview_row_pinterest);
		Button button_more = (Button) row
				.findViewById(R.id.button_row_pinterest);
		TextView textView_collect = (TextView) row
				.findViewById(R.id.textview_row_pinterest_collect);
		TextView textView_comment = (TextView) row
				.findViewById(R.id.textview_row_pinterest_comment);
		TextView textView_price = (TextView) row
				.findViewById(R.id.textview_row_pinterest_price);

		// 设置图片宽高
		LayoutParams layoutParams = imageView_background.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = height;
		imageView_background.setLayoutParams(layoutParams);

		// 加载图片
		ImageLoader.getInstance().displayImage(imageUrl, imageView_background,
				displayImageOptions);
		// 判断用户状态
		if (isMe) {// 当前用户
			textView_price.setVisibility(View.GONE);
			textView_collect.setText(entityGoodsSelling.getCollectNum());
			textView_comment.setText(entityGoodsSelling.getCommentNum());
			button_more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editPopWindow = new EditPopWindow(getActivity(),
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									switch (v.getId()) {
									case R.id.button_edit_delete:
										editPopWindow.dismiss();
										Toast.makeText(getActivity(), "正在删除",
												Toast.LENGTH_SHORT).show();
										new CloudAPI().PublishSellingDelete(
												entityGoodsSelling.getId(),
												new OnPostRequest<EntityBase>() {
													@Override
													public void onPostOk(
															EntityBase temp) {
														Toast.makeText(
																getActivity(),
																"删除成功",
																Toast.LENGTH_SHORT)
																.show();
														new CloudAPI()
																.UserGoodsSelling(
																		uid,
																		pageIndex,
																		displayNumber,
																		new GoodsSelling());
													}

													@Override
													public void onPostFailure(
															String err) {
														Toast.makeText(
																getActivity(),
																err,
																Toast.LENGTH_SHORT)
																.show();
													}
												});
										break;
									case R.id.button_edit_edit:
										editPopWindow.dismiss();
										EntityPublishSelling entityPublishSelling = new EntityPublishSelling();
										entityPublishSelling
												.setCid(entityGoodsSelling
														.getCid());
										entityPublishSelling
												.setMessage(entityGoodsSelling
														.getMessage());
										entityPublishSelling
												.setOriginalPrice(entityGoodsSelling
														.getOriginPrice());
										entityPublishSelling
												.setPrice(entityGoodsSelling
														.getPrice());
										entityPublishSelling
												.setSid(entityGoodsSelling
														.getId());
										entityPublishSelling
												.setSubject(entityGoodsSelling
														.getTitle());

										Intent intent = new Intent(
												getActivity(),
												ActivityPublishSelling.class);
										intent.putExtra("editselling",
												entityPublishSelling);
										startActivity(intent);
										break;
									}
								}
							});
					editPopWindow.showAtLocation(new View(getActivity()),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			});
		} else {// 非当前用户
			button_more.setVisibility(View.GONE);
			button_collect.setVisibility(View.INVISIBLE);
			imageView_comment.setVisibility(View.INVISIBLE);
			textView_price.setText("￥" + entityGoodsSelling.getPrice());
		}
		// 图片点击，跳转到商品详情
		imageView_background.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ActivityGoodsInfo.class);
				intent.putExtra("sid", entityGoodsSelling.getId());
				getActivity().startActivity(intent);
			}
		});
		return row;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		pageIndex = "1";
		displayNumber = "15";
		new CloudAPI().UserGoodsSelling(uid, pageIndex, displayNumber,
				new GoodsSelling());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (entityGoodsSellings.size() != 0) {
			int totle = entityGoodsSellings.get(0).getTotal();
			int index = Integer.parseInt(pageIndex) + 1;
			if (totle >= index) {
				pageIndex = index + "";
				new CloudAPI().UserGoodsSelling(uid, pageIndex, displayNumber,
						new GoodsSelling());
			} else {
				pullToRefreshScrollView.onPullUpRefreshComplete();
			}
		}
	}
}