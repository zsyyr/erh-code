package com.yidian_erhuo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityLogin;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsSearch;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterSearch extends BaseAdapter {
	private Activity activity;
	private DisplayImageOptions displayImageOptions;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityGoodsSearch> entityGoodsSearchs = new ArrayList<EntityGoodsSearch>();

	public ArrayList<EntityGoodsSearch> getEntityGoodsSearchs() {
		return entityGoodsSearchs;
	}

	public void setEntityGoodsSearchs(
			ArrayList<EntityGoodsSearch> entityGoodsSearchs) {
		viewMap = new HashMap<Integer, View>();
		this.entityGoodsSearchs = entityGoodsSearchs;
	}

	public AdapterSearch(Activity activity) {
		displayImageOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return entityGoodsSearchs.size();
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
					R.layout.view_row_search, null);
			viewHolder.imageView_goods = (ImageView) convertView
					.findViewById(R.id.imageview_row_search);
			viewHolder.textView_price = (TextView) convertView
					.findViewById(R.id.textview_row_search_price);
			viewHolder.textView_collect = (TextView) convertView
					.findViewById(R.id.textview_row_search_collect);
			viewHolder.textView_comment = (TextView) convertView
					.findViewById(R.id.textview_row_search_comment);
			viewHolder.textView_message = (TextView) convertView
					.findViewById(R.id.textview_row_search_message);
			viewHolder.textView_timetemp = (TextView) convertView
					.findViewById(R.id.textview_row_search_timetemp);
			viewHolder.imageView_collect = (ImageView) convertView
					.findViewById(R.id.imageview_row_search_collect);

			// 加载图片
			String imageUrl = "";
			if (0 < entityGoodsSearchs.get(position).getEntityImages().size()) {
				imageUrl = Utile.HTTP_IMAGE_URL
						+ entityGoodsSearchs.get(position).getEntityImages()
								.get(0).getImage();
			}
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageView_goods, displayImageOptions);
			// 加载其他数据
			viewHolder.textView_message.setText(entityGoodsSearchs
					.get(position).getTitle()
					+ "\t"
					+ entityGoodsSearchs.get(position).getMessage());
			viewHolder.textView_price.setText("￥"
					+ entityGoodsSearchs.get(position).getPrice());
			viewHolder.textView_timetemp.setText(Utile
					.getTime(entityGoodsSearchs.get(position)
							.getPublishTimeTemp()));
			viewHolder.textView_collect.setText(entityGoodsSearchs
					.get(position).getCollectNum());
			viewHolder.textView_comment.setText(entityGoodsSearchs
					.get(position).getCommentNum());
			// 收藏状态判断
			viewHolder.imageView_collect.setTag(entityGoodsSearchs
					.get(position).getCollect() + "");
			if (entityGoodsSearchs.get(position).getCollect() == 0) {
				viewHolder.imageView_collect
						.setImageResource(R.drawable.icon_collect);
			} else {
				viewHolder.imageView_collect
						.setImageResource(R.drawable.icon_collect_);
			}

			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 收藏点击事件
		final TextView textView = viewHolder.textView_collect;
		final ImageView imageView = viewHolder.imageView_collect;
		viewHolder.imageView_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != Utile.getUserInfo(activity)) {
					if ("0".equals(imageView.getTag())) {
						Toast.makeText(activity, "正在收藏", Toast.LENGTH_SHORT)
								.show();
						new CloudAPI().CollectAdd(
								entityGoodsSearchs.get(position).getId(), Utile
										.getUserInfo(activity).getId(),
								new OnPostRequest<EntityBase>() {
									@Override
									public void onPostOk(EntityBase temp) {
										imageView.setTag("1");
										int count = Integer.parseInt(textView
												.getText().toString()) + 1;
										textView.setText(count + "");
										imageView
												.setImageResource(R.drawable.icon_collect_);
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
								entityGoodsSearchs.get(position).getId(), Utile
										.getUserInfo(activity).getId(),
								new OnPostRequest<EntityBase>() {
									@Override
									public void onPostOk(EntityBase temp) {
										imageView.setTag("0");
										int count = Integer.parseInt(textView
												.getText().toString()) - 1;
										textView.setText(count + "");
										imageView
												.setImageResource(R.drawable.icon_collect);
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
		public ImageView imageView_goods, imageView_collect;
		public TextView textView_message, textView_price, textView_collect,
				textView_comment, textView_timetemp;

	}
}