package com.yidian_erhuo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityPublishBuying;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsBuying;
import com.yidian_erhuo.entity.EntityPublishBuying;
import com.yidian_erhuo.utile.EditPopWindow;
import com.yidian_erhuo.utile.SharePopWindow;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class AdapterUserinfoBuying extends BaseAdapter {
	private boolean isMe = false;

	private Activity activity;
	private EditPopWindow editPopWindow;
	private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
	private ArrayList<EntityGoodsBuying> entityGoodsBuyings = new ArrayList<EntityGoodsBuying>();

	public ArrayList<EntityGoodsBuying> getEntityGoodsBuyings() {
		return entityGoodsBuyings;
	}

	public void setEntityGoodsBuyings(
			ArrayList<EntityGoodsBuying> entityGoodsBuyings) {
		viewMap = new HashMap<Integer, View>();
		this.entityGoodsBuyings = entityGoodsBuyings;
	}

	public AdapterUserinfoBuying(Activity activity, boolean isMe) {
		this.isMe = isMe;
		this.activity = activity;
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
		ViewHolder viewHolder = null;
		convertView = viewMap.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LinearLayout.inflate(activity,
					R.layout.view_row_userinfo_buying, null);
			viewHolder.button_share = (Button) convertView
					.findViewById(R.id.button_userinfo_buying_share);
			viewHolder.textView_message = (TextView) convertView
					.findViewById(R.id.textview_userinfo_buying_message);
			viewHolder.textView_price = (TextView) convertView
					.findViewById(R.id.textview_userinfo_buying_price);
			viewHolder.textView_subject = (TextView) convertView
					.findViewById(R.id.textview_userinfo_buying_subject);
			viewHolder.textView_timetemp = (TextView) convertView
					.findViewById(R.id.textview_userinfo_buying_timetemp);

			viewHolder.textView_subject.setText(entityGoodsBuyings
					.get(position).getTitle());
			viewHolder.textView_message.setText(entityGoodsBuyings
					.get(position).getMessage());
			viewHolder.textView_timetemp.setText(Utile
					.getTime(entityGoodsBuyings.get(position)
							.getPublishTimeTemp()));
			viewHolder.textView_price.setText("￥"
					+ entityGoodsBuyings.get(position).getPrice());

			convertView.setTag(viewHolder);
			viewMap.put(position, convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 分享按钮
		viewHolder.button_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String shareMessage = entityGoodsBuyings.get(position)
						.getTitle()
						+ entityGoodsBuyings.get(position).getMessage()
						+ Utile.THIRD_SHARE_URL;
				new SharePopWindow(activity, shareMessage).showAtLocation(
						new View(activity), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
		if (isMe) {
			// 弹出编辑框
			((LinearLayout) convertView
					.findViewById(R.id.linearlayout_row_userinfo_buying))
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							editPopWindow = new EditPopWindow(activity,
									new OnClickListener() {
										@Override
										public void onClick(View v) {
											switch (v.getId()) {
											case R.id.button_edit_delete:
												editPopWindow.dismiss();
												Toast.makeText(activity,
														"正在删除",
														Toast.LENGTH_SHORT)
														.show();
												new CloudAPI()
														.PublishBuyingDelete(
																entityGoodsBuyings
																		.get(position)
																		.getId(),
																new OnPostRequest<EntityBase>() {
																	@Override
																	public void onPostOk(
																			EntityBase temp) {
																		Toast.makeText(
																				activity,
																				"删除成功",
																				Toast.LENGTH_SHORT)
																				.show();
																		entityGoodsBuyings
																				.remove(position);
																		viewMap.remove(position);
																		notifyDataSetChanged();
																	}

																	@Override
																	public void onPostFailure(
																			String err) {
																		Toast.makeText(
																				activity,
																				err,
																				Toast.LENGTH_SHORT)
																				.show();
																	}
																});
												break;
											case R.id.button_edit_edit:
												editPopWindow.dismiss();
												EntityPublishBuying entityPublishBuying = new EntityPublishBuying();
												entityPublishBuying
														.setSid(entityGoodsBuyings
																.get(position)
																.getId());
												entityPublishBuying
														.setPrice(entityGoodsBuyings
																.get(position)
																.getPrice());
												entityPublishBuying
														.setSubject(entityGoodsBuyings
																.get(position)
																.getTitle());
												entityPublishBuying
														.setMessage(entityGoodsBuyings
																.get(position)
																.getMessage());
												Intent intent = new Intent(
														activity,
														ActivityPublishBuying.class);
												intent.putExtra("editbuying",
														entityPublishBuying);
												activity.startActivity(intent);
												break;
											}
										}
									});
							editPopWindow.showAtLocation(new View(activity),
									Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
									0, 0);
						}
					});
		}
		return convertView;
	}

	class ViewHolder {
		public TextView textView_subject, textView_timetemp, textView_message,
				textView_price;
		public Button button_share;

	}
}