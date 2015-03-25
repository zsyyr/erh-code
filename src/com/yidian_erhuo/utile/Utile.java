package com.yidian_erhuo.utile;

import java.io.File;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Environment;

import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.wheelView.OnWheelChangedListener;
import com.yidian_erhuo.wheelView.OnWheelScrollListener;
import com.yidian_erhuo.wheelView.WheelAdapter;
import com.yidian_erhuo.wheelView.WheelView;

public class Utile {
	// 服务器图片请求地址
	public final static String HTTP_IMAGE_URL = "http://123.57.132.230/images/";
	// 服务器请求地址
	public final static String HTTP_API_URL = "http://123.57.132.230/Interface.php";
	// 用户注册
	public final static String HTTP_API_REGISTER = "user_reg";
	// 登陆
	public final static String HTTP_API_LOGIN = "user_login";
	// 第三方登陆
	public final static String HTTP_API_LOGIN_THIRD = "user_login_third";
	// 用户信息
	public final static String HTTP_API_USER_INFO = "user_info";
	// 密码修改
	public final static String HTTP_API_EDIT_PASSWORD = "pwd_update";
	// 用户信息修改
	public final static String HTTP_API_EDIT_USER_INFO = "user_update";
	// 首页焦点图（推荐商品）
	public final static String HTTP_API_GOODS_FOCUS = "recommend_goods_list";
	// 商品分类
	public final static String HTTP_API_GOODS_CLASSIFY = "class_list";
	// 正在卖商品列表
	public final static String HTTP_API_GOODS_SELLING = "sell_goods_list";
	// 商品详情
	public final static String HTTP_API_GOODS_INFO = "sell_goods_details";
	// 评论列表
	public final static String HTTP_API_GOODS_COMMENT = "comment_list";
	// 在卖商品搜索
	public final static String HTTP_API_GOODS_SEARCH = "search_sell_goods";
	// 求购商品列表
	public final static String HTTP_API_GOODS_BUYING = "shopping_goods_list";
	// 我的在卖列表
	public final static String HTTP_API_GOODS_SELLING_MY = "my_sell_goods";
	// 我的求购列表
	public final static String HTTP_API_GOODS_BUYING_MY = "my_shopping_goods";
	// 添加收藏
	public final static String HTTP_API_COLLECT_ADD = "insert_collect";
	// 取消收藏
	public final static String HTTP_API_COLLECT_REMOVE = "cancel_collect";
	// 我的收藏列表
	public final static String HTTP_API_COLLECT_MY = "my_collect";
	// 添加评论
	public final static String HTTP_API_COMMENT_ADD = "insert_comment";
	// 删除评论
	public final static String HTTP_API_COMMENT_REMOVE = "del_comment";
	// 我的评论列表
	public final static String HTTP_API_COMMENT_MY = "my_comment";
	// 发布求购
	public final static String HTTP_API_PUBLISH_BUYING = "insert_shopping_goods";
	// 发布商品
	public final static String HTTP_API_PUBLISH_SELLING = "insert_sell_goods";
	// 修改发布商品
	public final static String HTTP_API_PUBLISH_SELLING_UPDATE = "update_sell_goods";
	// 删除发布商品
	public final static String HTTP_API_PUBLISH_SELLING_DELETE = "delete_sell_goods";
	// 修改求购商品
	public final static String HTTP_API_PUBLISH_BUYING_UPDATE = "update_shopping_goods";
	// 删除求购商品
	public final static String HTTP_API_PUBLISH_BUYING_DELETE = "delete_shopping_goods";

	// 新浪第三方key
	public final static String THIRD_KEY_SINA = "1184286925";
	public final static String THIRD_URL_SINA = "https://api.weibo.com/oauth2/default.html";
	public final static String THIRD_SECRET_SINA = "739d8f5e8fd61cd00d0b3992a56c2a92";
	// 腾讯第三方key
	public final static String THIRD_KEY_TENCENT = "1102928485";
	// 微信第三方key
	public final static String THIRD_KEY_WEICHAT = "wx64bc9b6d0b7e7800";
	// 腾讯微博第三方key
	public final static Long THIRD_KEY_WEIBO = (long) 1102928485;
	public final static String THIRD_SECRET_WEIBO = "0dqRQsHfYMlP5edy";
	// 人人第三方key
	public final static String THIRD_APPID_RENREN = "271893";
	public final static String THIRD_KEY_RENREN = "90161cb6e1ed4281b52bfa49c9611914";
	public final static String THIRD_SECRET_RENREN = "3740bd8923fd42398eb1cf1b477dcf68";
	// 第三方分享url
	public final static String THIRD_SHARE_URL = "http://123.57.132.230:3000";

	/**
	 * 获取用户信息
	 */
	public static EntityUserInfo getUserInfo(Context context) {
		return new DatabaseService(context).QueryUserInfo();
	}

	/**
	 * 初始化默认路径
	 */
	public static String FilePatchInitialize() {
		String sdPatch = "";
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			File sdDir = Environment.getExternalStorageDirectory();
			sdPatch = sdDir.toString() + "/erhuo/";
		}
		return sdPatch;
	}

	/**
	 * 通过时间戳计算已故去的时间
	 * 
	 * @return
	 */
	public static String getTime(String timeTemp) {
		String temp = "";
		try {
			long now = System.currentTimeMillis() / 1000;
			long publish = Long.parseLong(timeTemp);
			long diff = now - publish;
			long days = diff / (60 * 60 * 24);
			long hours = (diff - days * (60 * 60 * 24)) / (60 * 60);
			long minutes = (diff - days * (60 * 60 * 24) - hours * (60 * 60)) / 60;
			if (days > 0) {
				temp = days + "天前";
			} else if (hours > 0) {
				temp = hours + "小时前";
			} else {
				temp = minutes + "分钟前";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 轮盘查找
	 * 
	 * @param title
	 *            dialog标题
	 * @param label
	 *            单位
	 * @param arry
	 *            集合
	 * @param index
	 *            定位
	 * @param context
	 * @param onWheelChangedListener
	 *            监听
	 * @param onWheelScrollListener
	 *            监听
	 */
	public static void wheelDiaglog(String title, String label, String[] arry,
			int index, Context context,
			OnWheelChangedListener onWheelChangedListener,
			OnWheelScrollListener onWheelScrollListener) {
		Builder dialog = new AlertDialog.Builder(context);
		if (title != null) {
			dialog.setTitle(title);
		}
		dialog.setPositiveButton("完成", null);
		dialog.setView(wheelView(context, label, arry, index,
				onWheelChangedListener, onWheelScrollListener));
		dialog.show();
	}

	/**
	 * 初始化wheelview对象
	 * 
	 * @param context
	 * @param label
	 *            单位
	 * @param arry
	 *            集合
	 * @param index
	 *            定位
	 * @param onWheelChangedListener
	 *            监听
	 * @param onWheelScrollListener
	 *            监听
	 * @return
	 */
	private static WheelView wheelView(Context context, String label,
			final String[] arry, int index,
			OnWheelChangedListener onWheelChangedListener,
			OnWheelScrollListener onWheelScrollListener) {
		WheelView wheelView = new WheelView(context);
		wheelView.setCyclic(true);
		wheelView.setAdapter(new WheelAdapter() {
			@Override
			public int getMaximumLength() {
				int MaximumLength = 0;
				for (int i = 0; i < arry.length; i++) {
					if (MaximumLength < arry[i].length()) {
						MaximumLength = arry[i].length();
					}
				}
				return MaximumLength;
			}

			@Override
			public int getItemsCount() {
				return arry.length;
			}

			@Override
			public String getItem(int index) {
				return arry[index];
			}
		});
		// 单位
		if (label != null) {
			wheelView.setLabel(label);
		}
		// 定位
		if (index >= 0 && index < arry.length) {
			wheelView.setCurrentItem(index);
		}
		// 滚动值改变监听
		if (onWheelChangedListener != null) {
			wheelView.addChangingListener(onWheelChangedListener);
		}
		// 滚动监听
		if (onWheelScrollListener != null) {
			wheelView.addScrollingListener(onWheelScrollListener);
		}
		return wheelView;
	}
}