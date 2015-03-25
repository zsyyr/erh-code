package com.yidian_erhuo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.entity.EntityGoodsClassify;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterGrideClassify extends BaseAdapter {
	private int imageWidth = 0;

	private Activity activity;
	private DisplayImageOptions displayImageOptions;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityGoodsClassify> entityGoodsClassifies = new ArrayList<EntityGoodsClassify>();

	public AdapterGrideClassify(Activity activity,
			ArrayList<EntityGoodsClassify> entityGoodsClassifies) {
		displayImageOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		this.entityGoodsClassifies = entityGoodsClassifies;
		this.activity = activity;
	}

	public AdapterGrideClassify(int imageWidth, Activity activity,
			ArrayList<EntityGoodsClassify> entityGoodsClassifies) {
		this.imageWidth = imageWidth;
		displayImageOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		this.entityGoodsClassifies = entityGoodsClassifies;
		this.activity = activity;
	}

	public ArrayList<EntityGoodsClassify> getEntityGoodsClassifies() {
		return entityGoodsClassifies;
	}

	public void setEntityGoodsClassifies(
			ArrayList<EntityGoodsClassify> entityGoodsClassifies) {
		this.entityGoodsClassifies = entityGoodsClassifies;
	}

	@Override
	public int getCount() {
		return entityGoodsClassifies.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		convertView = viewMap.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LinearLayout.inflate(activity,
					R.layout.view_row_tablerow_classify, null);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.imageview_classify);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.textview_classify);

			if (0 == imageWidth) {
				imageWidth = activity.getWindowManager().getDefaultDisplay()
						.getWidth() / 4 - 40;
			}
			LayoutParams layoutParams = viewHolder.imageView.getLayoutParams();
			layoutParams.width = imageWidth;
			layoutParams.height = imageWidth;
			viewHolder.imageView.setLayoutParams(layoutParams);

			// º”‘ÿÕº∆¨
			String imageUrl = Utile.HTTP_IMAGE_URL
					+ entityGoodsClassifies.get(position).getIcon();
			ImageLoader.getInstance().displayImage(imageUrl,
					viewHolder.imageView, displayImageOptions);
			// ÃÓ≥‰√˚◊÷
			viewHolder.textView.setText(entityGoodsClassifies.get(position)
					.getName());
			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
}