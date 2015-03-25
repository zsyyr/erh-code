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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityChat;
import com.yidian_erhuo.activity.ActivityLogin;
import com.yidian_erhuo.activity.ActivityUserInfo;
import com.yidian_erhuo.entity.EntityGoodsBuying;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.SharePopWindow;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterBuying extends BaseAdapter {
	private Activity activity;
	private DisplayImageOptions displayImageOptions;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityGoodsBuying> entityGoodsBuyings = new ArrayList<EntityGoodsBuying>();

	public AdapterBuying(Activity activity) {
		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		this.activity = activity;
	}

	public ArrayList<EntityGoodsBuying> getEntityGoodsBuyings() {
		return entityGoodsBuyings;
	}

	public void setEntityGoodsBuyings(
			ArrayList<EntityGoodsBuying> entityGoodsBuyings) {
		viewMap = new HashMap<Integer, View>();
		this.entityGoodsBuyings = entityGoodsBuyings;
	}

	@Override
	public int getCount() {
		return entityGoodsBuyings.size();
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

			convertView = LinearLayout.inflate(activity, R.layout.view_row_buy,
					null);
			viewHolder.imageButton = (ImageButton) convertView
					.findViewById(R.id.imagebutton_row_buy);
			viewHolder.button_chat = (Button) convertView
					.findViewById(R.id.button_row_buy_chat);
			viewHolder.button_share = (Button) convertView
					.findViewById(R.id.button_row_buy_share);
			viewHolder.textview_message = (TextView) convertView
					.findViewById(R.id.textview_row_buy_message);
			viewHolder.textview_price = (TextView) convertView
					.findViewById(R.id.textview_row_buy_price);
			viewHolder.textview_subject = (TextView) convertView
					.findViewById(R.id.textview_row_buy_subject);
			viewHolder.textview_timetemp = (TextView) convertView
					.findViewById(R.id.textview_row_buy_timetemp);
			viewHolder.linearLayout_bottom = (LinearLayout) convertView
					.findViewById(R.id.linearlayout_bottom);

			// 加载用户头像
			String imageUrl = Utile.HTTP_IMAGE_URL
					+ entityGoodsBuyings.get(position).getUserHeader();
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageButton, displayImageOptions);
			// 其他数据
			viewHolder.textview_price.setText("￥"
					+ entityGoodsBuyings.get(position).getPrice());
			viewHolder.textview_subject.setText(entityGoodsBuyings
					.get(position).getTitle());
			viewHolder.textview_message.setText(entityGoodsBuyings
					.get(position).getMessage());
			viewHolder.textview_timetemp.setText(Utile
					.getTime(entityGoodsBuyings.get(position)
							.getPublishTimeTemp()));

			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 聊天按钮
		viewHolder.button_chat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == Utile.getUserInfo(activity)) {
					activity.startActivity(new Intent(activity,
							ActivityLogin.class));
				} else if (!entityGoodsBuyings.get(position).getUserId()
						.equals(Utile.getUserInfo(activity).getId())) {
					EntityUserInfo entityUserInfo = new EntityUserInfo();
					entityUserInfo.setId(entityGoodsBuyings.get(position)
							.getUserId());
					entityUserInfo.setHeader(entityGoodsBuyings.get(position)
							.getUserHeader());
					entityUserInfo.setNickName(entityGoodsBuyings.get(position)
							.getUserNickName());

					Intent intent = new Intent(activity, ActivityChat.class);
					intent.putExtra("friendinfo", entityUserInfo);
					activity.startActivity(intent);
				}
			}
		});
		// 头像按钮
		viewHolder.imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EntityUserInfo entityUserInfo = Utile.getUserInfo(activity);
				if (null == entityUserInfo
						|| (!entityUserInfo.getId().equals(
								entityGoodsBuyings.get(position).getUserId()))) {
					Intent intent = new Intent(activity, ActivityUserInfo.class);
					intent.putExtra("uid", entityGoodsBuyings.get(position)
							.getUserId());
					activity.startActivity(intent);
				}
			}
		});
		// 分享按钮
		viewHolder.button_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shareMessage = entityGoodsBuyings.get(position)
						.getTitle()
						+ entityGoodsBuyings.get(position).getMessage()
						+ Utile.THIRD_SHARE_URL;
				new SharePopWindow(activity, shareMessage).showAtLocation(
						activity.findViewById(R.id.linearlayout_main),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
		return convertView;
	}

	class ViewHolder {
		public ImageButton imageButton;
		public Button button_chat, button_share;
		public TextView textview_subject, textview_timetemp, textview_message,
				textview_price;
		public LinearLayout linearLayout_bottom;
	}
}