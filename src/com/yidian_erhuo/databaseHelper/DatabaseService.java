package com.yidian_erhuo.databaseHelper;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yidian_erhuo.entity.EntityMessage;
import com.yidian_erhuo.entity.EntityUserInfo;

public class DatabaseService {
	private SQLiteDatabase sqLiteDatabase;
	private static DatabaseHelper databaseHelper;
	private static final String tableNameUserInfo = "userinfo";
	private static final String tableNameMessage = "message";

	/**
	 * 主构造，第一次调用的时候就实例化databasehelper对象
	 * 
	 * @param context
	 */
	public DatabaseService(Context context) {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(context);
		}
	}

	/**
	 * 插入用户信息
	 * 
	 * @param params
	 */
	public void InsertUserInfo(EntityUserInfo entityUserInfo) {
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		sqLiteDatabase.insert(tableNameUserInfo, null,
				getContentUserInfo(entityUserInfo));
		sqLiteDatabase.close();
	}

	/**
	 * 插入消息列表
	 * 
	 * @param entityUserInfo
	 */
	public void InsertMessage(EntityMessage entityMessage) {
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		sqLiteDatabase.insert(tableNameMessage, null,
				getContentMessage(entityMessage));
		sqLiteDatabase.close();
	}

	public void UpdataMessage(EntityMessage entityMessage) {
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		sqLiteDatabase.update(tableNameMessage,
				getContentMessage(entityMessage), "friendid = ?",
				new String[] { entityMessage.getFriendId() });
		sqLiteDatabase.close();
	}

	/**
	 * 删除用户信息
	 * 
	 * @param userId
	 */
	public void DeleteUserInfo() {
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		if (sqLiteDatabase.isOpen()) {
			sqLiteDatabase.delete(tableNameUserInfo, null, null);
			sqLiteDatabase.close();
		}
	}

	/**
	 * 删除用户信息，聊天列表
	 */
	public void DeleteAll() {
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		if (sqLiteDatabase.isOpen()) {
			sqLiteDatabase.delete(tableNameMessage, null, null);
			sqLiteDatabase.delete(tableNameUserInfo, null, null);
			sqLiteDatabase.close();
		}
	}

	/**
	 * 通过用户id修改用户信息
	 * 
	 * @param params
	 */
	public void UpdataUserInfo(EntityUserInfo entityUserInfo) {
		sqLiteDatabase = databaseHelper.getWritableDatabase();
		sqLiteDatabase.update(tableNameUserInfo,
				getContentUserInfo(entityUserInfo), "userid=?",
				new String[] { entityUserInfo.getId() });
		sqLiteDatabase.close();
	}

	/**
	 * 查询用户信息并封装成对象返回
	 */
	public EntityUserInfo QueryUserInfo() {
		EntityUserInfo entityUserInfo = null;
		sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query(tableNameUserInfo, null, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			String userId = cursor.getString(cursor.getColumnIndex("userid"));
			String userName = cursor.getString(cursor
					.getColumnIndex("username"));
			String nickName = cursor.getString(cursor
					.getColumnIndex("nickname"));
			String userHeader = cursor.getString(cursor
					.getColumnIndex("userheader"));
			String userSex = cursor.getString(cursor.getColumnIndex("usersex"));
			String userPhone = cursor.getString(cursor.getColumnIndex("phone"));
			String registerTimeTemp = cursor.getString(cursor
					.getColumnIndex("registertime"));
			String third = cursor.getString(cursor.getColumnIndex("third"));
			String openid = cursor.getString(cursor.getColumnIndex("openid"));

			entityUserInfo = new EntityUserInfo();
			entityUserInfo.setId(userId);
			entityUserInfo.setSex(userSex);
			entityUserInfo.setName(userName);
			entityUserInfo.setOpenId(openid);
			entityUserInfo.setPhone(userPhone);
			entityUserInfo.setThird_part(third);
			entityUserInfo.setNickName(nickName);
			entityUserInfo.setHeader(userHeader);
			entityUserInfo.setRegisterTime(registerTimeTemp);
		}
		sqLiteDatabase.close();
		return entityUserInfo;
	}

	public ArrayList<EntityMessage> QueryMessage() {
		ArrayList<EntityMessage> entityMessages = new ArrayList<EntityMessage>();
		sqLiteDatabase = databaseHelper.getReadableDatabase();
		Cursor cursor = sqLiteDatabase.query(tableNameMessage, null, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				String fid = cursor
						.getString(cursor.getColumnIndex("friendid"));
				String name = cursor.getString(cursor
						.getColumnIndex("friendnickname"));
				String header = cursor.getString(cursor
						.getColumnIndex("friendheader"));
				String msg = cursor.getString(cursor
						.getColumnIndex("lastmessage"));
				String msgtime = cursor.getString(cursor
						.getColumnIndex("lastmessagetimetemp"));

				EntityMessage entityMessage = new EntityMessage();
				entityMessage.setFriendId(fid);
				entityMessage.setLastMessage(msg);
				entityMessage.setFriendHeader(header);
				entityMessage.setFriendNickName(name);
				entityMessage.setLastMessageTimeTemp(msgtime);

				entityMessages.add(entityMessage);
				cursor.moveToNext();
			}
		}
		sqLiteDatabase.close();
		return entityMessages;
	}

	/**
	 * 得到用户封装对象
	 * 
	 * @param params
	 *            String数组
	 * @return ContentValues
	 */
	private ContentValues getContentUserInfo(EntityUserInfo entityUserInfo) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("userid", entityUserInfo.getId());
		contentValues.put("username", entityUserInfo.getName());
		contentValues.put("nickname", entityUserInfo.getNickName());
		contentValues.put("userheader", entityUserInfo.getHeader());
		contentValues.put("usersex", entityUserInfo.getSex());
		contentValues.put("phone", entityUserInfo.getPhone());
		contentValues.put("registertime", entityUserInfo.getRegisterTime());
		contentValues.put("third", entityUserInfo.getThird_part());
		contentValues.put("openid", entityUserInfo.getOpenId());
		return contentValues;
	}

	/**
	 * 得到消息列表封装对象
	 * 
	 * @param entityMessage
	 * @return
	 */
	private ContentValues getContentMessage(EntityMessage entityMessage) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("friendid", entityMessage.getFriendId());
		contentValues.put("friendnickname", entityMessage.getFriendNickName());
		contentValues.put("friendheader", entityMessage.getFriendHeader());
		contentValues.put("lastmessagetimetemp",
				entityMessage.getLastMessageTimeTemp());
		contentValues.put("lastmessage", entityMessage.getLastMessage());
		return contentValues;
	}
}