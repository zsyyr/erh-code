package com.yidian_erhuo.databaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int Version = 1;
	private static final String DB_NAME = "erhuo.db";
	// 创建用户表
	private static final String sql_userinfo = "create table userinfo(userid varchar(8) not null, username varchar(50) , nickname varchar(50) , userheader varchar(50) , usersex varchar(2) , phone varchar(20) , registertime varchar(40) , third varchar(20) , openid varchar(40));";
	// 消息列表
	private static final String sql_message = "create table message(friendid varchar(8) not null, friendnickname varchar(50)  , friendheader varchar(50) , lastmessagetimetemp varchar(40) , lastmessage varchar(40) );";

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, Version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sql_userinfo);
		db.execSQL(sql_message);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}