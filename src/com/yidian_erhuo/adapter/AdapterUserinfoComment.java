package com.yidian_erhuo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityGoodsInfo;
import com.yidian_erhuo.entity.EntityUserComment;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterUserinfoComment extends BaseAdapter {
	private Activity activity;
	private delete onDelete;
	private DisplayImageOptions displayImageOptions;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityUserComment> entityUserComments = new ArrayList<EntityUserComment>();

	public delete getOnDelete() {
		return onDelete;
	}

	public void setOnDelete(delete onDelete) {
		this.onDelete = onDelete;
	}

	public ArrayList<EntityUserComment> getEntityUserComments() {
		return entityUserComments;
	}

	public void setEntityUserComments(
			ArrayList<EntityUserComment> entityUserComments) {
		viewMap = new HashMap<Integer, View>();
		this.entityUserComments = entityUserComments;
	}

	public AdapterUserinfoComment(Activity activity) {
		displayImageOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.build();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return entityUserComments.size();
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
					R.layout.view_row_userinfo_comment, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageview_userinfo_comment);
			viewHolder.textView_subject = (TextView) convertView
					.findViewById(R.id.textview_userinfo_comment_subject);
			viewHolder.textView_messsage = (TextView) convertView
					.findViewById(R.id.textview_userinfo_comment_message);
			viewHolder.textView_timetemp = (TextView) convertView
					.findViewById(R.id.textview_userinfo_comment_timetemp);

			// 加载图片
			if (entityUserComments.get(position).getEntityImages().size() != 0) {
				String imageUrl = Utile.HTTP_IMAGE_URL
						+ entityUserComments.get(position).getEntityImages()
								.get(0).getImage();
				ImageLoader.getInstance().displayImage(imageUrl,
						viewHolder.imageView, displayImageOptions);
			}
			// 加载数据
			viewHolder.textView_messsage.setText(entityUserComments.get(
					position).getMessage());
			viewHolder.textView_timetemp.setText(Utile
					.getTime(entityUserComments.get(position).getTimeTemp()));

			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 点击
		((LinearLayout) convertView
				.findViewById(R.id.linearlayout_row_userinfo_comment))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(activity,
								ActivityGoodsInfo.class);
						intent.putExtra("sid", entityUserComments.get(position)
								.getSid());
						activity.startActivity(intent);
					}
				});
		// 长点击
		((LinearLayout) convertView
				.findViewById(R.id.linearlayout_row_userinfo_comment))
				.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						onDelete.onDelete(position);
						return true;
					}
				});
		return convertView;
	}

	public interface delete {
		void onDelete(int id);
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView textView_subject, textView_messsage, textView_timetemp;
	}
}