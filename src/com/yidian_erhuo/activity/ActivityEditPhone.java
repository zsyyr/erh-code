package com.yidian_erhuo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.utile.TitleView;

public class ActivityEditPhone extends ActivityBase {
	private String str_phone_old = "", str_phone_new = "";

	private TitleView titleView;
	private EditText editText_phone;

	@Override
	public void initView() {
		setContentView(R.layout.activity_edit_phone);

		// 接收传递过来的原始密码
		str_phone_old = getIntent().getStringExtra("phone");

		titleView = new TitleView(this);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		titleView.RightTextView("确定", 0);
		titleView.MidTextView("手机号", 0);

		editText_phone = (EditText) findViewById(R.id.edittext_edit_phone);
		if (str_phone_old != null) {
			editText_phone.setText(str_phone_old);
		}

		titleView.getTextView_right().setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		str_phone_new = editText_phone.getText().toString();
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.textview_title_right:
			if (!"".equals(str_phone_new)) {
				Intent data = new Intent();
				data.putExtra("phone", str_phone_new);
				setResult(ActivityPersonalCenter.EDIT_DATA, data);
				finish();
			} else {
				Toast.makeText(ActivityEditPhone.this, "手机号不能为空",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}