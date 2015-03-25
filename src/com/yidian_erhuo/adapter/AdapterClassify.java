package com.yidian_erhuo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityGoodsInfo;
import com.yidian_erhuo.activity.ActivityImageGallery;
import com.yidian_erhuo.activity.ActivityLogin;
import com.yidian_erhuo.activity.ActivityUserInfo;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsSelling;
import com.yidian_erhuo.entity.EntityImage;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.SharePopWindow;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterClassify extends BaseAdapter {
	private int imageWidth = 0;
	private int ScreenWidth = 0;

	private Activity activity;
	private DisplayImageOptions displayImageOptions, displayImageOptions_head;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityGoodsSelling> entityGoodsSellings = new ArrayList<EntityGoodsSelling>();

	public ArrayList<EntityGoodsSelling> getEntityGoodsSellings() {
		return entityGoodsSellings;
	}

	public void setEntityGoodsSellings(
			ArrayList<EntityGoodsSelling> entityGoodsSellings) {
		viewMap = new HashMap<Integer, View>();
		this.entityGoodsSellings = entityGoodsSellings;
	}

	public AdapterClassify(Activity activity) {
		ScreenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();
		imageWidth = ScreenWidth / 100 * 35;
		displayImageOptions_head = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		displayImageOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return entityGoodsSellings.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = viewMap.get(position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LinearLayout.inflate(activity,
					R.layout.view_row_classify, null);
			viewHolder.imageView_head = (ImageView) convertView
					.findViewById(R.id.imageview_row_classify);
			viewHolder.button_collect = (Button) convertView
					.findViewById(R.id.button_row_classify_collect);
			viewHolder.button_share = (ImageButton) convertView
					.findViewById(R.id.button_row_classify_share);
			viewHolder.textView_collect = (TextView) convertView
					.findViewById(R.id.textview_row_classify_collect);
			viewHolder.textView_comment = (TextView) convertView
					.findViewById(R.id.textview_row_classify_comment);
			viewHolder.textView_message = (TextView) convertView
					.findViewById(R.id.textview_row_classify_message);
			viewHolder.textView_price = (TextView) convertView
					.findViewById(R.id.textview_row_classify_price);
			viewHolder.textView_timetemp = (TextView) convertView
					.findViewById(R.id.textview_row_classify_timetemp);
			viewHolder.textView_username = (TextView) convertView
					.findViewById(R.id.textview_row_classify_username);
			viewHolder.linearLayout_gallery = (LinearLayout) convertView
					.findViewById(R.id.linearlayout_row_classify_gallery);

			// 加载头像
			String imageUrl = Utile.HTTP_IMAGE_URL
					+ entityGoodsSellings.get(position).getUserHeader();
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageView_head, displayImageOptions_head);
			// 加载其他数据
			viewHolder.textView_username.setText(entityGoodsSellings.get(
					position).getUserName());
			viewHolder.textView_timetemp.setText(Utile
					.getTime(entityGoodsSellings.get(position)
							.getPublishTimeTemp()));
			viewHolder.textView_message.setText(entityGoodsSellings.get(
					position).getTitle()
					+ "\t" + entityGoodsSellings.get(position).getMessage());
			viewHolder.textView_collect.setText(entityGoodsSellings.get(
					position).getCollectNum());
			viewHolder.textView_comment.setText(entityGoodsSellings.get(
					position).getCommentNum());
			viewHolder.textView_price.setText("￥"
					+ entityGoodsSellings.get(position).getPrice());
			// 添加图片列表
			ArrayList<EntityImage> entityImages = entityGoodsSellings.get(
					position).getEntityImages();
			LayoutParams layoutParams = viewHolder.textView_price
					.getLayoutParams();
			layoutParams.width = imageWidth;
			viewHolder.textView_price.setLayoutParams(layoutParams);
			for (int i = 0; i < entityImages.size(); i++) {
				final int j = i;
				String url = Utile.HTTP_IMAGE_URL
						+ entityImages.get(i).getImage();
				ImageView imageView = new ImageView(activity);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new LayoutParams(imageWidth,
						imageWidth));
				imageView.setClickable(true);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								ActivityImageGallery.class);
						intent.putExtra("id", j);
						intent.putExtra("entityImages", entityGoodsSellings
								.get(position).getEntityImages());
						activity.startActivity(intent);
					}
				});
				ImageLoader.getInstance().displayImage(url, imageView,
						displayImageOptions);
				viewHolder.linearLayout_gallery.addView(imageView);
				View view = new View(activity);
				view.setLayoutParams(new LayoutParams(20, 0));
				viewHolder.linearLayout_gallery.addView(view);
			}
			// 判断收藏状态
			viewHolder.button_collect.setTag(entityGoodsSellings.get(position)
					.getCollect() + "");
			if (entityGoodsSellings.get(position).getCollect() == 0) {
				viewHolder.button_collect
						.setBackgroundResource(R.drawable.icon_collect);
			} else {
				viewHolder.button_collect
						.setBackgroundResource(R.drawable.icon_collect_);
			}
			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// row点击事件
		((LinearLayout) convertView
				.findViewById(R.id.linearlayout_row_classify))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								ActivityGoodsInfo.class);
						intent.putExtra("sid", entityGoodsSellings
								.get(position).getId());
						activity.startActivity(intent);
					}
				});
		// 头像点击事件
		viewHolder.imageView_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EntityUserInfo entityUserInfo = Utile.getUserInfo(activity);
				if (null == entityUserInfo
						|| (!entityUserInfo.getId().equals(
								entityGoodsSellings.get(position).getUserId()))) {
					Intent intent = new Intent(activity, ActivityUserInfo.class);
					intent.putExtra("uid", entityGoodsSellings.get(position)
							.getUserId());
					activity.startActivity(intent);
				}
			}
		});
		// 分享点击事件
		viewHolder.button_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shareMessage = entityGoodsSellings.get(position)
						.getTitle()
						+ entityGoodsSellings.get(position).getMessage()
						+ Utile.THIRD_SHARE_URL;
				new SharePopWindow(activity, shareMessage).showAtLocation(
						activity.findViewById(R.id.linearlayout_classify),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
		// 收藏按钮点击
		final Button button = viewHolder.button_collect;
		final TextView textView = viewHolder.textView_collect;
		viewHolder.button_collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				if (null != Utile.getUserInfo(activity)) {
					if ("0".equals(button.getTag())) {
						Toast.makeText(activity, "正在收藏", Toast.LENGTH_SHORT)
								.show();
						new CloudAPI().CollectAdd(
								entityGoodsSellings.get(position).getId(),
								Utile.getUserInfo(activity).getId(),
								new OnPostRequest<EntityBase>() {
									@Override
									public void onPostOk(EntityBase temp) {
										button.setTag("1");
										int count = Integer.parseInt(textView
												.getText().toString()) + 1;
										textView.setText(count + "");
										button.setBackgroundResource(R.drawable.icon_collect_);
									}

									@Override
									public void onPostFailure(String err) {
										Toast.makeText(activity, "收藏失败，请重试",
												Toast.LENGTH_SHORT).show();
									}
								});
					} else {
						Toast.makeText(activity, "正在取消收藏", Toast.LENGTH_SHORT)
								.show();
						new CloudAPI().CollectRemove(
								entityGoodsSellings.get(position).getId(),
								Utile.getUserInfo(activity).getId(),
								new OnPostRequest<EntityBase>() {
									@Override
									public void onPostOk(EntityBase temp) {
										button.setTag("0");
										int count = Integer.parseInt(textView
												.getText().toString()) - 1;
										textView.setText(count + "");
										button.setBackgroundResource(R.drawable.icon_collect);
									}

									@Override
									public void onPostFailure(String err) {
										Toast.makeText(activity, "取消收藏失败，请重试",
												Toast.LENGTH_SHORT).show();
									}
								});
					}
				} else {
					activity.startActivity(new Intent(activity,
							ActivityLogin.class));
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		public ImageView imageView_head;
		public Button button_collect;
		public ImageButton button_share;
		public LinearLayout linearLayout_gallery;
		public TextView textView_username, textView_timetemp, textView_message,
				textView_price, textView_collect, textView_comment;
	}
}