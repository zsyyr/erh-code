package com.yidian_erhuo.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yidian_erhuo.R;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.AccessTokenKeeper;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("CommitPrefEdits")
public class ActivityLogin extends ActivityBase implements WeiboAuthListener,
		IUiListener {
	public final static int LOGIN_LOGOUT = 5;

	private Tencent tencent;
	private WeiboAuth weiboAuth;

	private TitleView titleView;
	private Button button_submit;
	private FrameLoading frameLoading;
	private TextView textView_register;
	private InputMethodManager inputMethodManager;
	private ImageButton imageButton_qq, imageButton_sina;
	private EditText editText_username, editText_password;

	@Override
	public void initView() {
		setContentView(R.layout.activity_login);

		frameLoading = new FrameLoading(this);
		titleView = new TitleView(this);
		titleView.MidTextView("登陆", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		button_submit = (Button) findViewById(R.id.button_login);
		imageButton_qq = (ImageButton) findViewById(R.id.imagebutton_login_qq);
		textView_register = (TextView) findViewById(R.id.textview_login_register);
		editText_username = (EditText) findViewById(R.id.edittext_login_username);
		imageButton_sina = (ImageButton) findViewById(R.id.imagebutton_login_sina);
		editText_password = (EditText) findViewById(R.id.edittext_login_userpassword);

		// 腾讯第三方
		tencent = Tencent.createInstance(Utile.THIRD_KEY_TENCENT,
				this.getApplicationContext());
		// 微博第三方
		weiboAuth = new WeiboAuth(this, new AuthInfo(this,
				Utile.THIRD_KEY_SINA, Utile.THIRD_URL_SINA,
				Utile.THIRD_SECRET_SINA));
		// 事件监听
		button_submit.setOnClickListener(this);
		imageButton_qq.setOnClickListener(this);
		imageButton_qq.setOnClickListener(this);
		imageButton_sina.setOnClickListener(this);
		textView_register.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_login:
			String str_username = editText_username.getText().toString();
			String str_userpassword = editText_password.getText().toString();
			if ("".equals(str_username)) {
				Toast.makeText(ActivityLogin.this, "用户名不能为空",
						Toast.LENGTH_SHORT).show();
			} else if ("".equals(str_userpassword)) {
				Toast.makeText(ActivityLogin.this, "密码不能为空", Toast.LENGTH_SHORT)
						.show();
			} 
			else {
				// 键盘隐藏
				inputMethodManager.hideSoftInputFromWindow(
						editText_password.getWindowToken(), 0);
				// 开启动画
				frameLoading.showFrame();
				frameLoading.setMessage("正在努力登录中");
				// 请求数据
				new CloudAPI().Login(str_username, str_userpassword,
						new OnPostRequest<EntityUserInfo>() {
							@Override
							public void onPostOk(EntityUserInfo temp) {
								loadUserInfo(temp);
							}

							@Override
							public void onPostFailure(String err) {
								frameLoading.hideFrame();
								Toast.makeText(ActivityLogin.this, err,
										Toast.LENGTH_SHORT).show();
							}
						});
			}
			break;
		case R.id.textview_login_register:
			startActivityForResult(new Intent(ActivityLogin.this,
					ActivityRegister.class), 0);
			break;
		case R.id.imagebutton_login_sina:// sina第三方登陆
			weiboAuth.anthorize(ActivityLogin.this);
			break;
		case R.id.imagebutton_login_qq:// QQ第三方登陆
			tencent.login(ActivityLogin.this, "all", ActivityLogin.this);
			break;
		case R.id.imagebutton_title_left:
			finish();
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == LOGIN_LOGOUT) {
			finish();
		}
		tencent.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void onCancel() {
		Toast.makeText(ActivityLogin.this, "已取消", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 新浪微博授权成功回调
	 */
	@Override
	public void onComplete(Bundle bundle) {
		Oauth2AccessToken oauth2AccessToken = Oauth2AccessToken
				.parseAccessToken(bundle);
		if (oauth2AccessToken.isSessionValid()) {
			AccessTokenKeeper.writeAccessToken(ActivityLogin.this,
					oauth2AccessToken);
			final String openId = bundle.getString("uid");
			UsersAPI usersAPI = new UsersAPI(oauth2AccessToken);
			usersAPI.show(Long.parseLong(openId), new RequestListener() {
				@Override
				public void onWeiboException(WeiboException arg0) {
					Toast.makeText(ActivityLogin.this, arg0.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onComplete(String arg0) {
					User user = User.parse(arg0);
					frameLoading.showFrame();
					frameLoading.setMessage("注册到服务器");
					new CloudAPI().LoginThird(openId, "sina", user.name,
							user.avatar_hd, "1",
							new OnPostRequest<EntityUserInfo>() {
								@Override
								public void onPostOk(EntityUserInfo temp) {
									loadUserInfo(temp);
								}

								@Override
								public void onPostFailure(String err) {
									frameLoading.hideFrame();
									Toast.makeText(ActivityLogin.this, err,
											Toast.LENGTH_SHORT).show();
								}
							});
				}
			});
		} else {
			Toast.makeText(ActivityLogin.this, "登陆失败", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		Toast.makeText(ActivityLogin.this, arg0.getMessage(),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * qq登录成功回调
	 */
	@Override
	public void onComplete(final Object object) {
		frameLoading.showFrame();
		frameLoading.setMessage("正在获取QQ信息");
		UserInfo userInfo = new UserInfo(ActivityLogin.this,
				tencent.getQQToken());
		userInfo.getUserInfo(new IUiListener() {
			@Override
			public void onError(UiError arg0) {
				Toast.makeText(ActivityLogin.this, arg0.errorMessage,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onComplete(Object obj) {
				try {
					String sex = "1";
					String openId = new JSONObject(object.toString())
							.getString("openid");
					JSONObject jsonObject = new JSONObject(obj.toString());
					String nickName = jsonObject.getString("nickname");
					String header = jsonObject.getString("figureurl_qq_1");

					frameLoading.setMessage("注册到服务器");
					new CloudAPI().LoginThird(openId, "qq", nickName, header,
							sex, new OnPostRequest<EntityUserInfo>() {

								@Override
								public void onPostOk(EntityUserInfo temp) {
									loadUserInfo(temp);
								}

								@Override
								public void onPostFailure(String err) {
									frameLoading.hideFrame();
									Toast.makeText(ActivityLogin.this, err,
											Toast.LENGTH_SHORT).show();
								}
							});
				} catch (JSONException e) {
					Toast.makeText(ActivityLogin.this, "获取id失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel() {
				Toast.makeText(ActivityLogin.this, "动作取消", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	public void onError(UiError arg0) {
		Toast.makeText(ActivityLogin.this, arg0.errorMessage,
				Toast.LENGTH_SHORT).show();
	}

	private void loadUserInfo(EntityUserInfo temp) {
		frameLoading.setMessage("正在获取用户数据");
		// 获取用户信息
		new CloudAPI().UserInfo(temp.getId(),
				new OnPostRequest<EntityUserInfo>() {
					@Override
					public void onPostOk(final EntityUserInfo temp) {
						// 登陆环信
						EMChatManager.getInstance().login(temp.getId(),
								temp.getId(), new EMCallBack() {
									@Override
									public void onSuccess() {
										// 将用户数据插入数据库
										new DatabaseService(ActivityLogin.this)
												.InsertUserInfo(temp);
										setResult(ActivityLogin.LOGIN_LOGOUT);
										// 退出登录页面
										finish();
									}

									@Override
									public void onProgress(int arg0, String arg1) {

									}

									@Override
									public void onError(int arg0, String arg1) {
										runOnUiThread(new Runnable() {
											public void run() {
												frameLoading.hideFrame();
												Toast.makeText(
														ActivityLogin.this,
														"聊天服务器登陆失败",
														Toast.LENGTH_SHORT)
														.show();
											}
										});
									}
								});
					}

					@Override
					public void onPostFailure(String err) {
						frameLoading.hideFrame();
						Toast.makeText(ActivityLogin.this, err,
								Toast.LENGTH_SHORT).show();
					}
				});

	}
}