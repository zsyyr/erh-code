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

import com.easemob.EMCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityUserInfo;
import com.yidian_erhuo.entity.EntityChat;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterChat extends BaseAdapter {
	private Activity activity;
	private DisplayImageOptions displayImageOptions;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityChat> entityChats = new ArrayList<EntityChat>();

	public ArrayList<EntityChat> getEntityChats() {
		return entityChats;
	}

	public void setEntityChats(ArrayList<EntityChat> entityChats) {
		this.entityChats = entityChats;
	}

	public AdapterChat(Activity activity) {
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
		return entityChats.size();
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
			if (entityChats.get(position).isMe()) {// 当前用户
				convertView = LinearLayout.inflate(activity,
						R.layout.view_row_chat_right, null);
			} else {// 好友
				convertView = LinearLayout.inflate(activity,
						R.layout.view_row_chat_left, null);
			}
			viewHolder.imageButton_header = (ImageButton) convertView
					.findViewById(R.id.imagebutton_row_chat);
			viewHolder.textView_chat_message = (TextView) convertView
					.findViewById(R.id.textview_row_chat);
			// viewHolder.imageButton_state = (ImageButton) convertView
			// .findViewById(R.id.imagebutton_row_chat_loading);

			// 加载头像
			String imageUrl = Utile.HTTP_IMAGE_URL
					+ entityChats.get(position).getHeader();
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageButton_header, displayImageOptions);
			// 加载内容
			viewHolder.textView_chat_message.setText(entityChats.get(position)
					.getMessage());

			// if (entityChats.get(position).isHistory()) {
			// viewHolder.imageButton_state.setVisibility(View.INVISIBLE);
			// } else if (entityChats.get(position).isMe()) {
			// AnimationDrawable animationDrawable = (AnimationDrawable)
			// viewHolder.imageButton_state
			// .getBackground();
			// animationDrawable.start();
			// EMChatManager.getInstance().sendMessage(
			// entityChats.get(position).getEmMessage(),
			// new emCallBack(viewHolder.imageButton_state));
			// }
			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 用户头像点击事件
		viewHolder.imageButton_header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!entityChats.get(position).isMe()) {
					Intent intent = new Intent(activity, ActivityUserInfo.class);
					intent.putExtra("uid", entityChats.get(position)
							.getUserId());
					activity.startActivity(intent);
				}
			}
		});
		// 加载按钮点击事件
		// final ImageButton imageButton = viewHolder.imageButton_state;
		// imageButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if ("1".equals(imageButton.getTag())) {
		// if (null != EMChatManager.getInstance()) {
		// EMChatManager.getInstance().sendMessage(
		// entityChats.get(position).getEmMessage(),
		// new emCallBack(imageButton));
		// }
		// }
		// }
		// });
		return convertView;
	}

	/**
	 * 消息发送监听
	 * 
	 * @author Administrator
	 * 
	 */
	class emCallBack implements EMCallBack {
		private ImageButton imageButton;

		public emCallBack(ImageButton imageButton) {
			this.imageButton = imageButton;
		}

		@Override
		public void onError(int arg0, String arg1) {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					imageButton
							.setBackgroundResource(android.R.drawable.ic_menu_rotate);
					imageButton.setTag("1");
				}
			});
		}

		@Override
		public void onProgress(int arg0, String arg1) {

		}

		@Override
		public void onSuccess() {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					imageButton.setVisibility(View.INVISIBLE);
					imageButton.setTag("");
				}
			});
		}

	}

	class ViewHolder {
		// imageButton_state
		public ImageButton imageButton_header;
		public TextView textView_chat_message;
	}
}