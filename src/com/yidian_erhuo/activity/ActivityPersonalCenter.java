package com.yidian_erhuo.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedVignetteBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.databaseHelper.DatabaseService;
import com.yidian_erhuo.entity.EntityUserInfo;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

public class ActivityPersonalCenter extends ActivityBase {
	public final static int EDIT_DATA = 6;
	private final static int REQUEST_CODE_IMAGE = 0;
	private final static int REQUEST_CODE_CAMERA = 1;
	private final static int REQUEST_CODE_HEADER_IMAGE = 2;

	private File headerFile;
	private FrameLoading frameLoading;
	private EntityUserInfo entityUserInfo;
	private TitleView titleView;
	private DisplayImageOptions displayImageOptions;
	private ImageButton imageButton_heading, imageButton_sex_boy,
			imageButton_sex_girl;
	private TextView textView_nickname, textView_phone, textView_id;
	private LinearLayout linearLayout_nickname, linearLayout_password,
			linearLayout_phone;
	private Button button_logout;

	@Override
	public void initView() {
		setContentView(R.layout.activity_personal_center);

		// 头像加载的配置信息
		displayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(100, 3)).build();
		frameLoading = new FrameLoading(this);
		titleView = new TitleView(this);
		titleView.MidTextView("个人中心", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);
		titleView.RightTextView("确定", 0);

//		textView_id = (TextView) findViewById(R.id.textview_personal_center_id);
		textView_phone = (TextView) findViewById(R.id.textview_personal_center_phone);
		imageButton_heading = (ImageButton) findViewById(R.id.imagebutton_personal_center);
		textView_nickname = (TextView) findViewById(R.id.textview_personal_center_nickname);
		linearLayout_phone = (LinearLayout) findViewById(R.id.linearlayout_personal_center_phone);
		imageButton_sex_boy = (ImageButton) findViewById(R.id.imagebutton_personal_center_sex_boy);
		imageButton_sex_girl = (ImageButton) findViewById(R.id.imagebutton_personal_center_sex_girl);
		linearLayout_nickname = (LinearLayout) findViewById(R.id.linearlayout_personal_center_nickname);
		linearLayout_password = (LinearLayout) findViewById(R.id.linearlayout_personal_center_password);
		button_logout = (Button) findViewById(R.id.button_personal_center);

		button_logout.setOnClickListener(this);
		linearLayout_nickname.setOnClickListener(this);
		linearLayout_password.setOnClickListener(this);
		linearLayout_phone.setOnClickListener(this);
		imageButton_heading.setOnClickListener(this);
		imageButton_sex_boy.setOnClickListener(this);
		imageButton_sex_girl.setOnClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);
		titleView.getTextView_right().setOnClickListener(this);
		// 获取用户信息
		entityUserInfo = Utile.getUserInfo(this);
		// 设置用户信息
//		textView_id.setText(entityUserInfo.getName());
		textView_nickname.setText(entityUserInfo.getNickName());
		if (!"0".equals(entityUserInfo.getPhone())) {
			textView_phone.setText(entityUserInfo.getPhone());
		}
		if ("1".equals(entityUserInfo.getSex())) {
			imageButton_sex_boy.setEnabled(false);
		} else {
			imageButton_sex_girl.setEnabled(false);
		}
		// 加载用户头像
		String imageUrl = Utile.HTTP_IMAGE_URL + entityUserInfo.getHeader();
		ImageLoader.getInstance().displayImage(imageUrl, imageButton_heading,
				displayImageOptions);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.textview_title_right:// 保存用户信息
			frameLoading.showFrame();
			String sex = "";
			if (imageButton_sex_boy.isEnabled()) {
				sex = "0";
			} else {
				sex = "1";
			}
			new CloudAPI().EditUserInfo(ActivityPersonalCenter.this,
					entityUserInfo.getId(), headerFile, sex, textView_nickname
							.getText().toString(), textView_phone.getText()
							.toString(), new EditUserInfo());
			break;
		case R.id.imagebutton_personal_center:// 修改用户头像
			ImageSelector();
			break;
		case R.id.imagebutton_personal_center_sex_boy:
			imageButton_sex_boy.setEnabled(false);
			imageButton_sex_girl.setEnabled(true);
			break;
		case R.id.imagebutton_personal_center_sex_girl:
			imageButton_sex_boy.setEnabled(true);
			imageButton_sex_girl.setEnabled(false);
			break;
		case R.id.button_personal_center:// 注销
			if (null != EMChatManager.getInstance()) {
				EMChatManager.getInstance().logout();
			}
			// 还原通知标记
			Editor editor = getSharedPreferences("erhuo", Activity.MODE_PRIVATE)
					.edit();
			editor.putBoolean("toast", true);
			editor.commit();
			new DatabaseService(ActivityPersonalCenter.this).DeleteAll();
			Toast.makeText(ActivityPersonalCenter.this, "注销成功",
					Toast.LENGTH_SHORT).show();
			setResult(ActivityLogin.LOGIN_LOGOUT);
			finish();
			break;
		case R.id.linearlayout_personal_center_nickname:
			/*intent = new Intent(ActivityPersonalCenter.this,
					ActivityEditNickName.class);
			intent.putExtra("nickname", textView_nickname.getText().toString());
			startActivityForResult(intent, 3);*/
			break;
		case R.id.linearlayout_personal_center_password:
			intent = new Intent(ActivityPersonalCenter.this,
					ActivityEditPassword.class);
			intent.putExtra("userId", entityUserInfo.getId());
			startActivity(intent);
			break;
		case R.id.linearlayout_personal_center_phone:
			intent = new Intent(ActivityPersonalCenter.this,
					ActivityEditPhone.class);
			intent.putExtra("phone", textView_phone.getText().toString());
			startActivityForResult(intent, 3);
			break;
		}
	}

	/**
	 * 修改用户信息回调
	 * 
	 * @author Administrator
	 * 
	 */
	class EditUserInfo implements OnPostRequest<EntityUserInfo> {

		@Override
		public void onPostOk(EntityUserInfo temp) {
			new DatabaseService(ActivityPersonalCenter.this).DeleteUserInfo();
			new DatabaseService(ActivityPersonalCenter.this)
					.InsertUserInfo(temp);
			Toast.makeText(ActivityPersonalCenter.this, "用户信息修改成功",
					Toast.LENGTH_SHORT).show();
			setResult(EDIT_DATA);
			finish();
		}

		@Override
		public void onPostFailure(String err) {
			frameLoading.hideFrame();
			Toast.makeText(ActivityPersonalCenter.this, err, Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (requestCode) {
			case REQUEST_CODE_IMAGE:
				startPhotoZoom(data.getData());
				break;
			case REQUEST_CODE_CAMERA:
				Uri uri = Uri.fromFile(new File(Utile.FilePatchInitialize()
						+ "heading_image_cache.jpg"));
				startPhotoZoom(uri);
				break;
			case REQUEST_CODE_HEADER_IMAGE:
				Bitmap bitmap = data.getExtras().getParcelable("data");
				new Thread(new ImageThread(bitmap)).start();
				break;
			}
			if (resultCode == EDIT_DATA) {
				String nickname = data.getStringExtra("nickname");
				String phone = data.getStringExtra("phone");
				if (null != nickname) {
					textView_nickname.setText(nickname);
				} else if (null != phone) {
					textView_phone.setText(phone);
				}
			}
		} else if (requestCode == REQUEST_CODE_CAMERA) {
			Uri uri = Uri.fromFile(new File(Utile.FilePatchInitialize()
					+ "heading_image_cache.jpg"));
			startPhotoZoom(uri);
		} else {
			Toast.makeText(ActivityPersonalCenter.this, "动作已取消",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 选择头像来源对话框
	 */
	private void ImageSelector() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("更换头像");
		dialog.setMessage("选择照片来源");
		dialog.setNegativeButton("相册", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent, REQUEST_CODE_IMAGE);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(ActivityPersonalCenter.this, "媒体库启动失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setPositiveButton("照相机", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(Utile.FilePatchInitialize()
									+ "heading_image_cache.jpg")));
					startActivityForResult(intent, REQUEST_CODE_CAMERA);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(ActivityPersonalCenter.this, "开启照相机失败",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
		dialog.show();
	}

	/**
	 * 图片裁剪
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_CODE_HEADER_IMAGE);
	}

	/**
	 * 添加图片 处理过程
	 * 
	 * @author Administrator
	 * 
	 */
	class ImageThread extends Thread {
		private Bitmap bitmap;

		public ImageThread(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		public void run() {
			if (bitmap != null) {
				String root = Utile.FilePatchInitialize();
				if (!root.equals("")) {
					try {
						String fileName = System.currentTimeMillis() / 1000
								+ ".jpg";
						headerFile = saveImage(bitmap,
								new File(root + fileName));
						runOnUiThread(new Runnable() {
							public void run() {
								RoundedVignetteBitmapDisplayer roundedVignetteBitmapDisplayer = new RoundedVignetteBitmapDisplayer(
										100, 5);
								roundedVignetteBitmapDisplayer.display(bitmap,
										imageButton_heading);
							}
						});
					} catch (IOException e) {
						Toast.makeText(ActivityPersonalCenter.this, "图片获取失败",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ActivityPersonalCenter.this, "SD卡不可用",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ActivityPersonalCenter.this, "图片获取失败",
						Toast.LENGTH_SHORT).show();
			}
		};

		/**
		 * 获取到的图片本地保存至指定目录
		 * 
		 * @param bitmap
		 *            原始图片
		 * @param file
		 *            文件路径
		 * @return
		 * @throws IOException
		 */
		private File saveImage(Bitmap bitmap, File file) throws IOException {
			// 压缩图片
			int options = 80;// 文件压缩比例
			int fileSize = 80;// 文件大小（KB）
			ByteArrayOutputStream btyeArrayOutputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, options,
					btyeArrayOutputStream);
			while (btyeArrayOutputStream.toByteArray().length / 1024 > fileSize) {
				btyeArrayOutputStream.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options,
						btyeArrayOutputStream);
				options -= 10;
				if (options <= 0) {
					break;
				}
			}
			// 判断缓存文件夹是否存在，不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// 将压缩后的图片写入到本地
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(btyeArrayOutputStream.toByteArray());
			fileOutputStream.flush();
			fileOutputStream.close();
			// 重赋值file
			return file;
		}
	}
}