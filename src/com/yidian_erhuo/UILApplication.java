package com.yidian_erhuo;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yidian_erhuo.cloud.IMReceiver;

public class UILApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// ImageLoader初始化
		initImageLoader(getApplicationContext());
		// IM初始化
		initIM(getApplicationContext());
	}

	private void initIM(final Context context) {
		// 初始化
		EMChat.getInstance().init(context);
		// 不显示状态栏
		EMChatOptions emChatOptions = EMChatManager.getInstance()
				.getChatOptions();
		emChatOptions.setShowNotificationInBackgroud(false);
		// 注册监听
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(new IMReceiver(), intentFilter);
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}
}