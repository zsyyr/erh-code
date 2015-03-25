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
		// ImageLoader��ʼ��
		initImageLoader(getApplicationContext());
		// IM��ʼ��
		initIM(getApplicationContext());
	}

	private void initIM(final Context context) {
		// ��ʼ��
		EMChat.getInstance().init(context);
		// ����ʾ״̬��
		EMChatOptions emChatOptions = EMChatManager.getInstance()
				.getChatOptions();
		emChatOptions.setShowNotificationInBackgroud(false);
		// ע�����
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