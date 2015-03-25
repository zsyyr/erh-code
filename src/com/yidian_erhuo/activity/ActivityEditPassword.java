package com.yidian_erhuo.activity;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.TitleView;

public class ActivityEditPassword extends ActivityBase {
	private String str_userId = "";

	private TitleView titleView;
	private FrameLoading frameLoading;
	private EditText editText_old, editText_new;
	private InputMethodManager inputMethodManager;

	@Override
	public void initView() {
		setContentView(R.layout.activity_edit_password);

		// 接收传递过来的原始密码
		str_userId = getIntent().getStringExtra("userId");
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		frameLoading = new FrameLoading(this);
		frameLoading.setBackground(R.color.background_gray);
		titleView = new TitleView(this);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		titleView.RightTextView("确定", 0);
		titleView.MidTextView("密码", 0);

		editText_new = (EditText) findViewById(R.id.edittext_edit_password_new);
		editText_old = (EditText) findViewById(R.id.edittext_edit_password_old);

		titleView.getTextView_right().setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String password_new = editText_new.getText().toString();
		String password_old = editText_old.getText().toString();
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.textview_title_right:
			if ("".equals(password_old)) {
				Toast.makeText(ActivityEditPassword.this, "请填写原始密码",
						Toast.LENGTH_SHORT).show();
			} else if ("".equals(password_new)) {
				Toast.makeText(ActivityEditPassword.this, "请填写新密码",
						Toast.LENGTH_SHORT).show();
			} else if (password_old.equals(password_new)) {
				Toast.makeText(ActivityEditPassword.this, "新旧密码不能相同",
						Toast.LENGTH_SHORT).show();
			} else {
				frameLoading.showFrame();
				inputMethodManager.hideSoftInputFromWindow(
						editText_new.getWindowToken(), 0);
				new CloudAPI().EditPassword(str_userId, password_old,
						password_new, new EditPassword());
			}
			break;
		}
	}

	class EditPassword implements OnPostRequest<EntityBase> {

		@Override
		public void onPostOk(EntityBase temp) {
			Toast.makeText(ActivityEditPassword.this, "密码修改成功",
					Toast.LENGTH_SHORT).show();
			frameLoading.hideFrame();
			finish();
		}

		@Override
		public void onPostFailure(String err) {
			frameLoading.hideFrame();
			Toast.makeText(ActivityEditPassword.this, err, Toast.LENGTH_SHORT)
					.show();
		}
	}
}