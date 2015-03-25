package com.yidian_erhuo.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.yidian_erhuo.R;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityUserInfo;

public class ActivityLoading extends ActivityBase {
	@Override
	public void initView() {
		setContentView(R.layout.activity_loading);
		// 1.2�����ת����ҳ
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				finish();
			}
		}, 1200);
		// ��¼������
		EntityUserInfo entityUserInfo = new DatabaseService(this)
				.QueryUserInfo();
		if (null != entityUserInfo) {
			EMChatManager.getInstance().login(entityUserInfo.getId(),
					entityUserInfo.getId(), new LoginInEM());
		}
	}

	class LoginInEM implements EMCallBack {
		@Override
		public void onError(int arg0, String arg1) {
			String uid = new DatabaseService(ActivityLoading.this)
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
}