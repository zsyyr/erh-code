package com.yidian_erhuo.cloud;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityChat;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityChat;
import com.yidian_erhuo.entity.EntityMessage;
import com.yidian_erhuo.entity.EntityUserInfo;

public class IMReceiver extends BroadcastReceiver {
	public static final int ID = 1;

	private static onChat onChat;
	private NotificationManager notificationManager;
	private static SharedPreferences sharedPreferences;

	public onChat getOnChat() {
		return onChat;
	}

	@SuppressWarnings("static-access")
	public void setOnChat(onChat onChat) {
		this.onChat = onChat;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 记录到消息队列中
		if (null == sharedPreferences && null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			sharedPreferences = context.getSharedPreferences("erhuo",
					Activity.MODE_PRIVATE);
		}
		// 发信人id
		String msgFrom = intent.getStringExtra("from");
		// 得到im对象
		String msgId = intent.getStringExtra("msgid");
		EMMessage emMessage = EMChatManager.getInstance().getMessage(msgId);
		// TextMessageBody textMessageBody = (TextMessageBody)
		// emMessage.getBody();
		// 设置适配器
		// EntityChat entityChat = json2Entity(textMessageBody.getMessage());
		EntityChat entityChat = new EntityChat();
		try {
			entityChat.setMessage(emMessage.getStringAttribute("message"));
			entityChat.setHeader(emMessage.getStringAttribute("fromheader"));
			entityChat.setName(emMessage.getStringAttribute("fromname"));
			entityChat.setUserId(msgFrom);
			entityChat.setMe(false);
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		if (null != onChat) {
			onChat.chat(entityChat);
		} else if (sharedPreferences.getBoolean("toast", true)) {// 判断是否需要通知栏通知
			// 发信人信息
			EntityUserInfo entityUserInfo = new EntityUserInfo();
			entityUserInfo.setId(msgFrom);
			entityUserInfo.setHeader(entityChat.getHeader());
			entityUserInfo.setName(entityChat.getName());
			// 跳转方法
			Intent in = new Intent(context, ActivityChat.class);
			in.putExtra("friendinfo", entityUserInfo);
			// 通知
			showNotification(context, entityUserInfo, entityChat, in);
		}
		// 判断是否需要插入数据库
		ArrayList<EntityMessage> entityMessages = new DatabaseService(context)
				.QueryMessage();
		boolean need = false;
		if (entityMessages.size() == 0) {
			need = true;
		} else {
			for (int i = 0; i < entityMessages.size(); i++) {
				if (entityMessages.get(i).getFriendId().equals(msgFrom)) {
					need = false;
					break;
				} else {
					need = true;
				}
			}
		}
		EntityMessage entityMessage = new EntityMessage();
		entityMessage.setFriendId(msgFrom);
		entityMessage.setFriendHeader(entityChat.getHeader());
		entityMessage.setLastMessage(entityChat.getMessage());
		entityMessage.setFriendNickName(entityChat.getName());
		entityMessage.setLastMessageTimeTemp(System.currentTimeMillis() / 1000
				+ "");
		// 插入数据库
		if (need) {
			new DatabaseService(context).InsertMessage(entityMessage);
		} else {
			new DatabaseService(context).UpdataMessage(entityMessage);
		}
	}

	/**
	 * 通知
	 * 
	 * @param context
	 * @param entityUserInfo
	 * @param entityChat
	 * @param intent
	 */
	private void showNotification(Context context,
			EntityUserInfo entityUserInfo, EntityChat entityChat, Intent intent) {
		// 实例化对象服务
		notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// 设置参数
		Notification notification = new Notification(R.drawable.ic_launcher,
				entityChat.getMessage(), System.currentTimeMillis());
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 5000; // 闪光时间，毫秒
		// 通知跳转
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		// 设置内容
		notification.setLatestEventInfo(context, entityChat.getName(),
				entityChat.getMessage(), pendingIntent);
		notificationManager.notify(ID, notification);
	}

	// /**
	// * json转换成封装对象
	// */
	// private EntityChat json2Entity(String json) {
	// EntityChat entityChat = new EntityChat();
	// try {
	// JSONObject jsonObject = new JSONObject(json);
	// entityChat.setMe(false);
	// entityChat.setHeader(jsonObject.getString("header"));
	// entityChat.setMessage(jsonObject.getString("message"));
	// entityChat.setNickName(jsonObject.getString("nickname"));
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return entityChat;
	// }

	/**
	 * 回调
	 * 
	 * @author Administrator
	 * 
	 */
	public interface onChat {
		void chat(EntityChat entityChat);
	}
}