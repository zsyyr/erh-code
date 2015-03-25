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
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityChat;
import com.yidian_erhuo.entity.EntityMessage;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterMessage extends BaseAdapter {
	private Activity activity;
	private DisplayImageOptions displayImageOptions;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityMessage> entityMessages = new ArrayList<EntityMessage>();

	public ArrayList<EntityMessage> getEntityMessages() {
		return entityMessages;
	}

	public void setEntityMessages(ArrayList<EntityMessage> entityMessages) {
		viewMap = new HashMap<Integer, View>();
		this.entityMessages = entityMessages;
	}

	public AdapterMessage(Activity activity) {
		displayImageOptions = new DisplayImageOptions.Builder()
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
		return entityMessages.size();
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
		ViewHolder viewHolder = null;
		convertView = viewMap.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LinearLayout.inflate(activity,
					R.layout.view_row_message, null);
			viewHolder.imagebutton = (ImageButton) convertView
					.findViewById(R.id.imagebutton_row_message);
			viewHolder.textView_brief = (TextView) convertView
					.findViewById(R.id.textview_row_message_brief);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textview_row_message_name);
			viewHolder.textView_timetemp = (TextView) convertView
					.findViewById(R.id.textview_row_message_timetemp);

			// 加载数据
			String imageUrl = Utile.HTTP_IMAGE_URL
					+ entityMessages.get(position).getFriendHeader();
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imagebutton, displayImageOptions);
			viewHolder.textView_brief.setText(entityMessages.get(position)
					.getLastMessage());
			viewHolder.textView_name.setText(entityMessages.get(position)
					.getFriendNickName());
			viewHolder.textView_timetemp.setText(Utile.getTime(entityMessages
					.get(position).getLastMessageTimeTemp()));

			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 点击事件
		((LinearLayout) convertView.findViewById(R.id.linearlayout_row_message))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (null != EMChatManager.getInstance()) {
							EntityMessage entityMessage = getEntityMessages()
									.get(position);
							Intent intent = new Intent(activity,
									ActivityChat.class);

							EntityUserInfo entityUserInfo = new EntityUserInfo();
							entityUserInfo.setId(entityMessage.getFriendId());
							entityUserInfo.setHeader(entityMessage
									.getFriendHeader());
							entityUserInfo.setName(entityMessage
									.getFriendNickName());

							intent.putExtra("friendinfo", entityUserInfo);
							activity.startActivity(intent);
						} else {
							Toast.makeText(activity, "聊天服务器登陆失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		return convertView;
	}

	class ViewHolder {
		public ImageButton imagebutton;
		public TextView textView_name, textView_timetemp, textView_brief;
	}
}