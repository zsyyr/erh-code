package com.yidian_erhuo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityPublishBuying;
import com.yidian_erhuo.fragment.FragmentBuying;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

public class ActivityPublishBuying extends ActivityBase {
	private TitleView titleView;
	private Button button_sutmit;
	private FrameLoading frameLoading;
	private EntityPublishBuying entityPublishBuying;
	private EditText editText_subject, editText_price, editText_message;

	@Override
	public void initView() {
		setContentView(R.layout.activity_publish_buying);

		entityPublishBuying = (EntityPublishBuying) getIntent()
				.getSerializableExtra("editbuying");
		frameLoading = new FrameLoading(this);
		titleView = new TitleView(this);
		titleView.MidTextView("发布求购", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);

		button_sutmit = (Button) findViewById(R.id.button_publish_buying);
		editText_message = (EditText) findViewById(R.id.edittext_publish_buying_message);
		editText_price = (EditText) findViewById(R.id.edittext_publish_buying_price);
		editText_subject = (EditText) findViewById(R.id.edittext_publish_buying_subject);

		button_sutmit.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
		// 如果是编辑模式，则填充数据
		if (null != entityPublishBuying) {
			editText_price.setText(entityPublishBuying.getPrice());
			editText_subject.setText(entityPublishBuying.getSubject());
			editText_message.setText(entityPublishBuying.getMessage());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.button_publish_buying:
			String str_price = editText_price.getText().toString();
			String str_subject = editText_subject.getText().toString();
			String str_message = editText_message.getText().toString();
			if ("".equals(str_subject)) {
				Toast.makeText(ActivityPublishBuying.this, "宝贝标题不能为空",
						Toast.LENGTH_SHORT).show();
			} else if ("".equals(str_price)) {
				Toast.makeText(ActivityPublishBuying.this, "宝贝价格不能为空",
						Toast.LENGTH_SHORT).show();
			} else if ("".equals(str_message)) {
				Toast.makeText(ActivityPublishBuying.this, "宝贝描述不能为空",
						Toast.LENGTH_SHORT).show();
			} else if (null != entityPublishBuying) {// 编辑模式
				frameLoading.showFrame();
				new CloudAPI().PublishBuyingUpdate(
						entityPublishBuying.getSid(), str_subject, str_message,
						str_price, new PublishBuying());
			} else {// 发布模式
				frameLoading.showFrame();
				new CloudAPI().PublishBuying(str_subject, str_message,
						str_price, Utile
								.getUserInfo(ActivityPublishBuying.this)
								.getId(), new PublishBuying());
			}
			break;
		}
	}

	/**
	 * 发布求购商品
	 * 
	 * @author Administrator
	 * 
	 */
	class PublishBuying implements OnPostRequest<EntityBase> {

		@Override
		public void onPostOk(EntityBase temp) {
			Toast.makeText(ActivityPublishBuying.this, "求购发布成功",
					Toast.LENGTH_SHORT).show();
			setResult(FragmentBuying.PUBLISH_OK);
			finish();
		}

		@Override
		public void onPostFailure(String err) {
			frameLoading.hideFrame();
			Toast.makeText(ActivityPublishBuying.this, err, Toast.LENGTH_SHORT)
					.show();
		}
	}
}