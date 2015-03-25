package com.yidian_erhuo.utile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.status.StatusSetRequestParam;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.sdk.android.api.WeiboAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.Authorize;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.network.HttpCallback;
import com.yidian_erhuo.R;
import com.yidian_erhuo.wxapi.WXEntryActivity;
import com.yidian_erhuo.wxapi.WXEntryActivity.WXRequest;

@SuppressLint("ClickableViewAccessibility")
public class SharePopWindow extends PopupWindow implements OnClickListener,
		OnTouchListener {
	// 第三方接口对象
	private static Renren renren;
	private static IWXAPI iwxapi;
	private static Tencent tencent;
	private static WeiboAuth weiboAuth;
	private static StatusesAPI statusesAPI;
	// 第三方发送需要填写的内容
	private String ShareMessage = "";

	private Activity activity;
	private View contentView;
	private OnShareWindowDismiss onShareWindowDismiss;
	private LinearLayout linearLayout_background, linearLayout_view;

	public SharePopWindow setOnShareWindowDismiss(
			OnShareWindowDismiss onShareWindowDismiss) {
		this.onShareWindowDismiss = onShareWindowDismiss;
		return this;
	}

	@SuppressLint("ClickableViewAccessibility")
	public SharePopWindow(Activity activity, String ShareMessage) {
		super(activity);
		// 加载view
		initView(activity);
		// 共享context
		this.activity = activity;
		// 要分享的内容
		this.ShareMessage = ShareMessage;
		// 设置SelectPicPopupWindow的View
		this.setContentView(contentView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(new ColorDrawable(0x00000000));
	}

	/**
	 * 加载视图
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		contentView = LinearLayout.inflate(context, R.layout.view_share, null);
		// 微信
		((ImageButton) contentView.findViewById(R.id.imagebutton_share_qq_chat))
				.setOnClickListener(this);
		// 朋友圈
		((ImageButton) contentView
				.findViewById(R.id.imagebutton_share_qq_friend))
				.setOnClickListener(this);
		// 人人
		((ImageButton) contentView.findViewById(R.id.imagebutton_share_renren))
				.setOnClickListener(this);
		// 新浪
		((ImageButton) contentView.findViewById(R.id.imagebutton_share_sina))
				.setOnClickListener(this);
		// qq空间
		((ImageButton) contentView
				.findViewById(R.id.imagebutton_share_qq_space))
				.setOnClickListener(this);
		// 腾讯微博
		((ImageButton) contentView
				.findViewById(R.id.imagebutton_share_qq_weibo))
				.setOnClickListener(this);
		// 取消按钮
		((Button) contentView.findViewById(R.id.button_share_cancel))
				.setOnClickListener(this);
		// 背景
		linearLayout_background = (LinearLayout) contentView
				.findViewById(R.id.linearlayout_share_background);
		linearLayout_background.setOnTouchListener(this);
		// 分享view
		linearLayout_view = (LinearLayout) contentView
				.findViewById(R.id.linearlayout_share_view);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		// 事件监听
		switch (v.getId()) {
		case R.id.button_share_cancel:// 取消按钮
			dismissAnimation();
			break;
		case R.id.imagebutton_share_qq_chat:// 微信
			if (null == iwxapi) {// 如果对象为空，则注册对象
				iwxapi = WXAPIFactory.createWXAPI(activity,
						Utile.THIRD_KEY_WEICHAT, true);
				iwxapi.registerApp(Utile.THIRD_KEY_WEICHAT);
			}
			if (iwxapi.isWXAppSupportAPI()) {
				WXTextObject textObject_chat = new WXTextObject(ShareMessage);
				WXMediaMessage wxMediaMessage_chat = new WXMediaMessage();
				wxMediaMessage_chat.mediaObject = textObject_chat;
				wxMediaMessage_chat.description = ShareMessage;
				SendMessageToWX.Req req_chat = new SendMessageToWX.Req();
				req_chat.transaction = String.valueOf(System
						.currentTimeMillis());
				req_chat.message = wxMediaMessage_chat;
				iwxapi.sendReq(req_chat);
				new WXEntryActivity().setWxRequest(new WXRequest() {
					@Override
					public void request(Intent intent) {

					}
				});
				Toast.makeText(activity, "正在分享到微信", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity, "无法分享到微信", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.imagebutton_share_qq_friend:// 朋友圈
			if (null == iwxapi) {// 如果对象为空，则注册对象
				iwxapi = WXAPIFactory.createWXAPI(activity,
						Utile.THIRD_KEY_WEICHAT, true);
				iwxapi.registerApp(Utile.THIRD_KEY_WEICHAT);
			}
			if (iwxapi.isWXAppSupportAPI()) {
				iwxapi.openWXApp();
				iwxapi.handleIntent(activity.getIntent(), new WeiChatHandler());
				WXTextObject textObject_friend = new WXTextObject(ShareMessage);
				WXMediaMessage wxMediaMessage_friend = new WXMediaMessage();
				wxMediaMessage_friend.mediaObject = textObject_friend;
				wxMediaMessage_friend.description = ShareMessage;
				SendMessageToWX.Req req_friend = new SendMessageToWX.Req();
				req_friend.transaction = String.valueOf(System
						.currentTimeMillis());
				req_friend.scene = SendMessageToWX.Req.WXSceneTimeline;
				req_friend.message = wxMediaMessage_friend;
				iwxapi.sendReq(req_friend);
				new WXEntryActivity().setWxRequest(new WXRequest() {
					@Override
					public void request(Intent intent) {

					}
				});
				Toast.makeText(activity, "正在分享到朋友圈", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(activity, "无法分享到朋友圈", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.imagebutton_share_renren:// 人人
			if (null == renren) {
				renren = new Renren(Utile.THIRD_KEY_RENREN,
						Utile.THIRD_SECRET_RENREN, Utile.THIRD_APPID_RENREN,
						activity);
			}
			new Thread(new Runnable() {
				public void run() {
					renren.publishStatus(activity, new StatusSetRequestParam(
							ShareMessage));
				}
			}).start();
			Toast.makeText(activity, "正在分享到人人", Toast.LENGTH_SHORT).show();
			break;
		case R.id.imagebutton_share_sina:// 新浪
			if (null == statusesAPI) {
				weiboAuth = new WeiboAuth(activity, new AuthInfo(activity,
						Utile.THIRD_KEY_SINA, Utile.THIRD_URL_SINA,
						Utile.THIRD_SECRET_SINA));
				statusesAPI = new StatusesAPI(
						AccessTokenKeeper.readAccessToken(activity));
			}
			statusesAPI.update(ShareMessage, "0.0", "0.0", new ShareSina());
			Toast.makeText(activity, "正在分享到新浪", Toast.LENGTH_SHORT).show();
			break;
		case R.id.imagebutton_share_qq_space:// qq空间
			if (null == tencent) {
				tencent = Tencent.createInstance(Utile.THIRD_KEY_TENCENT,
						activity);
			}
			Bundle bundle = new Bundle();
			bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "贰货");
			bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
					"http://123.57.132.230/download.php?id=qq");
			bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, ShareMessage);
			tencent.shareToQzone(activity, bundle, new IUiListener() {
				@Override
				public void onError(UiError arg0) {
					Toast.makeText(activity, "分享失败:" + arg0, Toast.LENGTH_SHORT)
							.show();
				}

				@Override
				public void onComplete(Object arg0) {
					Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onCancel() {

				}
			});
			Toast.makeText(activity, "正在分享到qq空间", Toast.LENGTH_SHORT).show();
			break;
		case R.id.imagebutton_share_qq_weibo:// 腾讯微博
			String accessToken = Util.getSharePersistent(
					activity.getApplicationContext(), "ACCESS_TOKEN");
			if (null == accessToken || "".equals(accessToken)) {
				AuthHelper.register(activity, Utile.THIRD_KEY_WEIBO,
						Utile.THIRD_SECRET_WEIBO, new OnAuthListener() {
							@Override
							public void onWeiboVersionMisMatch() {
								AuthHelper.unregister(activity);
								activity.startActivity(new Intent(activity,
										Authorize.class));
							}

							@Override
							public void onWeiBoNotInstalled() {
								AuthHelper.unregister(activity);
								activity.startActivity(new Intent(activity,
										Authorize.class));
							}

							@Override
							public void onAuthPassed(String arg0,
									WeiboToken weiboToken) {
								Util.saveSharePersistent(activity,
										"ACCESS_TOKEN", weiboToken.accessToken);
								Util.saveSharePersistent(activity,
										"EXPIRES_IN",
										String.valueOf(weiboToken.expiresIn));
								Util.saveSharePersistent(activity, "OPEN_ID",
										weiboToken.openID);
								Util.saveSharePersistent(activity,
										"REFRESH_TOKEN", "");
								Util.saveSharePersistent(activity, "CLIENT_ID",
										Util.getConfig().getProperty("APP_KEY"));
								Util.saveSharePersistent(activity,
										"AUTHORIZETIME", String.valueOf(System
												.currentTimeMillis() / 1000l));
								AuthHelper.unregister(activity);
								// 授权成功
								AccountModel accountModel = new AccountModel(
										weiboToken.accessToken);
								WeiboAPI weiboAPI = new WeiboAPI(accountModel);
								weiboAPI.addWeibo(activity, ShareMessage,
										"json", 0.0, 0.0, 0, 0, new ShareTc(),
										null, BaseVO.TYPE_JSON);
								Toast.makeText(activity, "分享成功",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onAuthFail(int arg0, String arg1) {
								Toast.makeText(activity, "授权失败",
										Toast.LENGTH_SHORT).show();
							}
						});
				AuthHelper.auth(activity, "");
			} else {
				// 授权成功
				AccountModel accountModel = new AccountModel(
						Util.getSharePersistent(
								activity.getApplicationContext(),
								"ACCESS_TOKEN"));
				WeiboAPI weiboAPI = new WeiboAPI(accountModel);
				weiboAPI.addWeibo(activity, ShareMessage, "json", 0.0, 0.0, 0,
						0, new ShareTc(), null, BaseVO.TYPE_JSON);
				Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	class ShareTc implements HttpCallback {

		@Override
		public void onResult(Object arg0) {

		}
	}

	class ShareSina implements RequestListener {
		@Override
		public void onWeiboException(WeiboException arg0) {
			weiboAuth.anthorize(new WeiboAuthListener() {
				@Override
				public void onWeiboException(WeiboException arg0) {
					Toast.makeText(activity, "认证失败", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onComplete(Bundle bundle) {
					Oauth2AccessToken oauth2AccessToken = Oauth2AccessToken
							.parseAccessToken(bundle);
					if (oauth2AccessToken.isSessionValid()) {
						AccessTokenKeeper.writeAccessToken(activity,
								oauth2AccessToken);
						statusesAPI = new StatusesAPI(AccessTokenKeeper
								.readAccessToken(activity));
						statusesAPI.update(ShareMessage, "0.0", "0.0",
								new ShareSina());
					} else {
						Toast.makeText(activity, "认证失败", Toast.LENGTH_SHORT)
								.show();
					}
				}

				@Override
				public void onCancel() {
					Toast.makeText(activity, "认证取消", Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onComplete(String arg0) {
			Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		dismissAnimation();
		return true;
	}

	/**
	 * 微信handler回调
	 * 
	 * @author Administrator
	 * 
	 */
	class WeiChatHandler implements IWXAPIEventHandler {

		@Override
		public void onReq(BaseReq arg0) {
			System.out.println("");
		}

		@Override
		public void onResp(BaseResp arg0) {
			System.out.println("");
		}
	}

	@SuppressWarnings("static-access")
	private void dismissAnimation() {
		Animation animation_out = new AnimationUtils().loadAnimation(activity,
				R.anim.anim_share_out);
		linearLayout_view.startAnimation(animation_out);
		animation_out.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				linearLayout_view.setVisibility(View.INVISIBLE);
				linearLayout_background.startAnimation(new AnimationUtils()
						.loadAnimation(activity, R.anim.anim_share_bg_out));
			}
		});
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (null != onShareWindowDismiss) {
					onShareWindowDismiss.dismiss();
				}
				dismiss();
			}
		}, 400);
	}

	@Override
	@SuppressWarnings("static-access")
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		linearLayout_view.setVisibility(View.INVISIBLE);
		Animation animation_in = new AnimationUtils().loadAnimation(activity,
				R.anim.anim_share_bg_in);
		linearLayout_background.startAnimation(animation_in);
		animation_in.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				linearLayout_view.setVisibility(View.VISIBLE);
				linearLayout_view.startAnimation(new AnimationUtils()
						.loadAnimation(activity, R.anim.anim_share_in));
			}
		});
	}

	/**
	 * 取消窗口时事件监听
	 * 
	 * @author Administrator
	 * 
	 */
	public interface OnShareWindowDismiss {
		void dismiss();
	}
}