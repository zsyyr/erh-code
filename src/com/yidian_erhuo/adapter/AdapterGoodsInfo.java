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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityUserInfo;
import com.yidian_erhuo.entity.EntityGoodsComment;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterGoodsInfo extends BaseAdapter {
	private Activity activity;
	private DisplayImageOptions displayImageOptions_head;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityGoodsComment> entityGoodsComments = new ArrayList<EntityGoodsComment>();

	public HashMap<Integer, View> getViewMap() {
		return viewMap;
	}

	public void setViewMap(HashMap<Integer, View> viewMap) {
		this.viewMap = viewMap;
	}

	public ArrayList<EntityGoodsComment> getEntityGoodsComments() {
		return entityGoodsComments;
	}

	public void setEntityGoodsComments(
			ArrayList<EntityGoodsComment> entityGoodsComments) {
		viewMap = new HashMap<Integer, View>();
		this.entityGoodsComments = entityGoodsComments;
	}

	public AdapterGoodsInfo(Activity activity) {
		displayImageOptions_head = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return entityGoodsComments.size();
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
					R.layout.view_row_goods_comment, null);
			viewHolder.imageButton_head = (ImageButton) convertView
					.findViewById(R.id.imagebutton_goods_info_head);
			viewHolder.textView_mesage = (TextView) convertView
					.findViewById(R.id.textview_goods_info_message);
			viewHolder.textView_timetemp = (TextView) convertView
					.findViewById(R.id.textview_goods_info_timetemp);
			viewHolder.textView_username = (TextView) convertView
					.findViewById(R.id.textview_goods_info_username);

			// 加载用户头像
			String imageUrl = Utile.HTTP_IMAGE_URL
					+ entityGoodsComments.get(position).getUserHeader();
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageButton_head, displayImageOptions_head);
			// 加载其他数据
			viewHolder.textView_username.setText(entityGoodsComments.get(
					position).getUserNickName());
			viewHolder.textView_mesage.setText(entityGoodsComments
					.get(position).getMessage());
			viewHolder.textView_timetemp.setText(Utile
					.getTime(entityGoodsComments.get(position).getTimeTemp()));

			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// 用户头像点击
		viewHolder.imageButton_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EntityUserInfo entityUserInfo = Utile.getUserInfo(activity);
				if (null == entityUserInfo
						|| (!entityUserInfo.getId().equals(
								entityGoodsComments.get(position).getUserId()))) {
					Intent intent = new Intent(activity, ActivityUserInfo.class);
					intent.putExtra("uid", entityGoodsComments.get(position)
							.getUserId());
					activity.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		public ImageButton imageButton_head;
		public TextView textView_username, textView_timetemp, textView_mesage;
	}
}