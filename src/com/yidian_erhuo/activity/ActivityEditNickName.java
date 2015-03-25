package com.yidian_erhuo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.utile.TitleView;

public class ActivityEditNickName extends ActivityBase {
	private String str_nickname_old = "", str_nickname_new = "";

	private TitleView titleView;
	private EditText editText_nickname;

	@Override
	public void initView() {
		setContentView(R.layout.activity_edit_nickname);

		// ���մ��ݹ�����ԭʼ����
		str_nickname_old = getIntent().getStringExtra("nickname");

		titleView = new TitleView(this);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		titleView.RightTextView("ȷ��", 0);
		titleView.MidTextView("�ǳ�", 0);

		editText_nickname = (EditText) findViewById(R.id.edittext_edit_nickname);
		if (str_nickname_old != null) {
			editText_nickname.setText(str_nickname_old);
		}

		titleView.getTextView_right().setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		str_nickname_new = editText_nickname.getText().toString();
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.textview_title_right:
			if (!"".equals(str_nickname_new)) {
				Intent data = new Intent();
				data.putExtra("nickname", str_nickname_new);
				setResult(ActivityPersonalCenter.EDIT_DATA, data);
				finish();
			}
			else if (str_nickname_new.length() < 4 || str_nickname_new.length() > 15){
				Toast.makeText(ActivityEditNickName.this, "�û���Ӧ����Ϊ4��15���ַ�/����",
						Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(ActivityEditNickName.this, "�ǳƲ���Ϊ��",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
}