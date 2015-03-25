package com.yidian_erhuo.activity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.yidian_erhuo.R;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.TitleView;

public class ActivityRegister extends ActivityBase {
	private TitleView titleView;
	private Button button_submit;
	private FrameLoading frameLoading;
	private InputMethodManager inputMethodManager;
	private EditText editText_username, editText_userpassword;

	@Override
	public void initView() {
		setContentView(R.layout.activity_register);

		frameLoading = new FrameLoading(this);
		titleView = new TitleView(this);
		titleView.MidTextView("ע��", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		button_submit = (Button) findViewById(R.id.button_register);
		editText_username = (EditText) findViewById(R.id.edittext_register_username);
		editText_userpassword = (EditText) findViewById(R.id.edittext_register_userpassword);

		editText_username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable edt) {
				// TODO Auto-generated method stub
				String temp = edt.toString();
				int i;
				for(i = 0;i<temp.length();i++){
					
		        	int mid = temp.charAt(i);
		        	if(!(mid>=48&&mid<=57) && !(mid>=97&&mid<=122)&& !(mid>=65&&mid<=90)){//���֡�Сд����д��ĸ
		        		Toast.makeText(ActivityRegister.this, "�û���ֻ��Ϊ��ĸ������",Toast.LENGTH_LONG).show();
		        		editText_username.setText("");
		        		return;
		        	}
				}
			}
		});
		
		button_submit.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register:
			String str_username = editText_username.getText().toString();
			String str_userpassword = editText_userpassword.getText()
					.toString();

			if ("".equals(str_username)) {
				Toast.makeText(ActivityRegister.this, "�û�������Ϊ��",
						Toast.LENGTH_SHORT).show();
			} 
			else if (str_username.length() < 4 || str_username.length() > 15){
				Toast.makeText(ActivityRegister.this, "�û���Ӧ����Ϊ4��15λ��ĸ������",
						Toast.LENGTH_SHORT).show();
			}
			else if ("".equals(str_userpassword)) {
				Toast.makeText(ActivityRegister.this, "���벻��Ϊ��",
						Toast.LENGTH_SHORT).show();
			}
			else if (str_userpassword.length() < 6 || str_userpassword.length() > 20){
				Toast.makeText(ActivityRegister.this, "����Ӧ����Ϊ6��20λ��ĸ������",
						Toast.LENGTH_SHORT).show();
			}
			else {
				frameLoading.showFrame();
				frameLoading.setMessage("����Ŭ��ע����");
				// ��������
				inputMethodManager.hideSoftInputFromWindow(
						editText_userpassword.getWindowToken(), 0);
				new CloudAPI().Register(str_username, str_userpassword,
						new OnPostRequest<EntityUserInfo>() {
							@Override
							public void onPostOk(EntityUserInfo temp) {
								// �رն���
								frameLoading.setMessage("���������û���Ϣ");
								// ��ȡ�û���Ϣ
								new CloudAPI().UserInfo(temp.getId(),
										new OnPostRequest<EntityUserInfo>() {
											@Override
											public void onPostOk(
													final EntityUserInfo temp) {
												// ��½����
												EMChatManager
														.getInstance()
														.login(temp.getId(),
																temp.getId(),
																new LoginInEM());
												// ���û����ݲ������ݿ�
												new DatabaseService(
														ActivityRegister.this)
														.InsertUserInfo(temp);
												// �˳���¼ҳ��
												setResult(ActivityLogin.LOGIN_LOGOUT);
												finish();
											}

											@Override
											public void onPostFailure(String err) {
												frameLoading.hideFrame();
												Toast.makeText(
														ActivityRegister.this,
														err, Toast.LENGTH_SHORT)
														.show();
											}
										});
							}

							@Override
							public void onPostFailure(String err) {
								frameLoading.hideFrame();
								Toast.makeText(ActivityRegister.this, err,
										Toast.LENGTH_SHORT).show();
							}
						});
			}
			break;
		case R.id.imagebutton_title_left:
			KeyBack();
			break;
		}
	}

	class LoginInEM implements EMCallBack {

		@Override
		public void onError(int arg0, String arg1) {
			String uid = new DatabaseService(ActivityRegister.this)
					.QueryUserInfo().getId();
			// ��½����
			EMChatManager.getInstance().login(uid, uid, new LoginInEM());
		}

		@Override
		public void onProgress(int arg0, String arg1) {

		}

		@Override
		public void onSuccess() {

		}
	}

	@Override
	public void KeyBack() {
		setResult(2);
		finish();
	}
}