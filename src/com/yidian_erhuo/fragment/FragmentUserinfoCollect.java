package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityImage;
import com.yidian_erhuo.entity.EntityUserCollect;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase;
import com.yidian_erhuo.pullToRefresh.PullToRefreshBase.OnRefreshListener;
import com.yidian_erhuo.pullToRefresh.PullToRefreshScrollView;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("ClickableViewAccessibility")
public class FragmentUserinfoCollect extends FragmentBase implements
		OnRefreshListener<ScrollView> {
	private String uid = "";
	private int ScreenWidth = 0;

	private ScrollView scrollView;
	private FrameLoading frameLoading;
	private int leftHeight = 0, rightHeight = 0;
	private DisplayImageOptions displayImageOptions;
	private PullToRefreshScrollView pullToRefreshScrollView;
	private LinearLayout linearlayout_left, linearlayout_right;

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
		pullToRefreshScrollView.setPullLoadEnabled(false);
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
	}

	@Override
	public void onRefresh() {
		frameLoading.showFrame();
		uid = Utile.getUserInfo(getActivity()).getId();
		new CloudAPI().UserGoodsCollect(uid, new GoodsCollect());
	}

	/**
	 * 用户收藏列表
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsCollect implements OnPostRequest<ArrayList<EntityUserCollect>> {

		@Override
		public void onPostOk(ArrayList<EntityUserCollect> temp) {
			// 清空所有数据
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
			new CloudAPI().UserGoodsCollect(uid, new GoodsCollect());
		}
	}

	private View addRow(final EntityUserCollect entityUserCollect,
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
		// 加载其他数据
		button_more.setVisibility(View.INVISIBLE);
		textView_price.setVisibility(View.INVISIBLE);
		textView_comment.setVisibility(View.INVISIBLE);
		imageView_comment.setVisibility(View.INVISIBLE);
		textView_collect.setText(entityUserCollect.getCollectNum());
		button_collect.setBackgroundResource(R.drawable.icon_collect_);

		// 取消收藏
		button_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "正在取消收藏", Toast.LENGTH_SHORT)
						.show();
				new CloudAPI().CollectRemove(entityUserCollect.getSid(), Utile
						.getUserInfo(getActivity()).getId(),
						new OnPostRequest<EntityBase>() {
							@Override
							public void onPostOk(EntityBase temp) {
								new CloudAPI().UserGoodsCollect(uid,
										new GoodsCollect());
								Toast.makeText(getActivity(), "取消收藏成功",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onPostFailure(String err) {
								Toast.makeText(getActivity(), err,
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
		// 点击图片跳转详情
		imageView_background.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						ActivityGoodsInfo.class);
				intent.putExtra("sid", entityUserCollect.getSid());
				getActivity().startActivity(intent);
			}
		});
		return row;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		new CloudAPI().UserGoodsCollect(uid, new GoodsCollect());
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

	}
}