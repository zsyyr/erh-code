package com.yidian_erhuo.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yidian_erhuo.R;
import com.yidian_erhuo.adapter.AdapterGrideClassify;
import com.yidian_erhuo.cloud.CloudAPI;
import com.yidian_erhuo.cloud.CloudAPI.OnPostRequest;
import com.yidian_erhuo.entity.EntityBase;
import com.yidian_erhuo.entity.EntityGoodsClassify;
import com.yidian_erhuo.entity.EntityPublishSelling;
import com.yidian_erhuo.utile.FrameLoading;
import com.yidian_erhuo.utile.ObservableGridView;
import com.yidian_erhuo.utile.SharePopWindow;
import com.yidian_erhuo.utile.SharePopWindow.OnShareWindowDismiss;
import com.yidian_erhuo.utile.TitleView;
import com.yidian_erhuo.utile.Utile;

@SuppressLint("UseSparseArrays")
public class ActivityPublishSelling extends ActivityBase implements
		OnItemClickListener {
	private int id = 0;
	private String sid = "";
	private String cid = "";
	private boolean isJump = false;
	private String shareTitle = "";
	private String shareMessage = "";
	private final static int REQUEST_CODE_IMAGE = 0;
	private final static int REQUEST_CODE_CAMERA = 1;

	private View line;
	private View imageManager;
	private TitleView titleView;
	private Button button_submit;
	private ImageButton imageButton;
	private FrameLoading frameLoading;
	private TextView textView_classify;
	private LinearLayout linearLayout_classify;
	private ObservableGridView observableGridView;
	private EntityPublishSelling entityPublishSelling;
	private ArrayList<EntityGoodsClassify> entityGoodsClassifies;
	private HashMap<Integer, File> fileImages = new HashMap<Integer, File>();
	private EditText editText_subject, editText_price_new, editText_price_old,
			editText_message;

	private ArrayList<ImageView> imageViews_cancel = new ArrayList<ImageView>();
	private ArrayList<ImageButton> imageButtons_image = new ArrayList<ImageButton>();

	@Override
	public void initView() {
		setContentView(R.layout.activity_publish_selling);

		entityPublishSelling = (EntityPublishSelling) getIntent()
				.getSerializableExtra("editselling");

		frameLoading = new FrameLoading(this);
		titleView = new TitleView(this);
		titleView.MidTextView("��Ҫ��", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);

		line = (View) findViewById(R.id.line);
		imageManager = (View) findViewById(R.id.view_image_manager);
		button_submit = (Button) findViewById(R.id.button_publish_selling);
		imageButton = (ImageButton) findViewById(R.id.imagebutton_publish_selling);
		editText_message = (EditText) findViewById(R.id.edittext_publish_selling_message);
		editText_subject = (EditText) findViewById(R.id.edittext_publish_selling_subject);
		textView_classify = (TextView) findViewById(R.id.textview_publish_selling_classify);
		observableGridView = (ObservableGridView) findViewById(R.id.gridview_publish_selling);
		editText_price_new = (EditText) findViewById(R.id.edittext_publish_selling_price_new);
		editText_price_old = (EditText) findViewById(R.id.edittext_publish_selling_price_old);
		linearLayout_classify = (LinearLayout) findViewById(R.id.linearlayout_publish_selling_classify);

		imageButton.setOnClickListener(this);
		button_submit.setOnClickListener(this);
		linearLayout_classify.setOnClickListener(this);
		observableGridView.setOnItemClickListener(this);
		titleView.getImageButton_left().setOnClickListener(this);

		setData();
		// ���ط����б�
		new CloudAPI().GoodsClassify(new GoodsClassify());
		// ���ر༭ģʽ������
		if (null != entityPublishSelling) {
			// �������
			editText_subject.setText(entityPublishSelling.getSubject());
			editText_price_new.setText(entityPublishSelling.getPrice());
			editText_message.setText(entityPublishSelling.getMessage());
			editText_price_old.setText(entityPublishSelling.getOriginalPrice());
			// ����ͼƬ����
			((FrameLayout) findViewById(R.id.framelayout_publish_selling))
					.setVisibility(View.GONE);
		}
	}

	/**
	 * �����б�
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsClassify implements
			OnPostRequest<ArrayList<EntityGoodsClassify>> {

		@Override
		public void onPostOk(ArrayList<EntityGoodsClassify> temp) {
			entityGoodsClassifies = temp;
			if (null != entityPublishSelling) {
				for (int i = 0; i < temp.size(); i++) {
					if (temp.get(i).getId()
							.equals(entityPublishSelling.getCid())) {
						textView_classify.setText(temp.get(i).getName());
						break;
					}
				}
			}
			observableGridView.setAdapter(new AdapterGrideClassify(110,
					ActivityPublishSelling.this, temp));
		}

		@Override
		public void onPostFailure(String err) {
			new CloudAPI().GoodsClassify(new GoodsClassify());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		case R.id.imagebutton_publish_selling:
			imageManager.setVisibility(View.VISIBLE);
			break;
		case R.id.button_publish_selling:
			String str_subject = editText_subject.getText().toString();
			String str_message = editText_message.getText().toString();
			String str_price_new = editText_price_new.getText().toString();
			String str_price_old = editText_price_old.getText().toString();
			if ("".equals(str_subject)) {
				Toast.makeText(ActivityPublishSelling.this, "�������ⲻ��Ϊ��",
						Toast.LENGTH_SHORT).show();
			} else if ("".equals(str_price_new)) {
				Toast.makeText(ActivityPublishSelling.this, "ת�ü۸���Ϊ��",
						Toast.LENGTH_SHORT).show();
			} else if (fileImages.size() == 0 && null == entityPublishSelling) {
				Toast.makeText(ActivityPublishSelling.this, "�������ϴ�һ��ͼƬ",
						Toast.LENGTH_SHORT).show();
			} else if ("".equals(cid)) {
				Toast.makeText(ActivityPublishSelling.this, "��ѡ��һ������",
						Toast.LENGTH_SHORT).show();
				linearLayout_classify.setTag("0");
				observableGridView.setVisibility(View.VISIBLE);
			} else if (null != entityPublishSelling) {// �༭ģʽ
				frameLoading.showFrame();
				new CloudAPI().PublishSellingUpdate(
						entityPublishSelling.getSid(), cid, str_subject,
						str_message, str_price_new, str_price_old,
						new GoodsPublishSelling());
			} else {// ����ģʽ
				shareTitle = str_subject;
				shareMessage = str_message;
				frameLoading.showFrame();
				new CloudAPI().PublishSelling(
						Utile.getUserInfo(ActivityPublishSelling.this).getId(),
						cid, str_subject, str_message, str_price_new,
						str_price_old, fileImages, new GoodsPublishSelling());
			}
			break;
		case R.id.linearlayout_publish_selling_classify:
			if ("0".equals(linearLayout_classify.getTag())) {
				linearLayout_classify.setTag("1");
				line.setVisibility(View.GONE);
				observableGridView.setVisibility(View.GONE);
			} else {
				linearLayout_classify.setTag("0");
				line.setVisibility(View.VISIBLE);
				observableGridView.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	/**
	 * ������Ϣ
	 * 
	 * @author Administrator
	 * 
	 */
	class GoodsPublishSelling implements OnPostRequest<EntityBase> {

		@Override
		public void onPostOk(EntityBase temp) {
			sid = temp.getSid();
			Toast.makeText(ActivityPublishSelling.this, "�����ɹ�",
					Toast.LENGTH_SHORT).show();
			String ShareMessage = "�����Ʒ����Ӵ~" + shareTitle + shareMessage
					+ Utile.THIRD_SHARE_URL;
			new SharePopWindow(ActivityPublishSelling.this, ShareMessage)
					.setOnShareWindowDismiss(new OnShareWindowDismiss() {

						@Override
						public void dismiss() {
							if (!isJump) {
								isJump = true;
								Intent intent = new Intent(
										ActivityPublishSelling.this,
										ActivityGoodsInfo.class);
								intent.putExtra("sid", sid);
								startActivity(intent);
								finish();
							}
						}
					})
					.showAtLocation(
							ActivityPublishSelling.this
									.findViewById(R.id.framelayout_publish),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		@Override
		public void onPostFailure(String err) {
			frameLoading.hideFrame();
			Toast.makeText(ActivityPublishSelling.this, err, Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * ����ͼƬѡ��/���յ����ݻص�
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri uri = null;
		if (resultCode == -1) {
			switch (requestCode) {
			case REQUEST_CODE_IMAGE:
				uri = data.getData();
				new Thread(new ImageThread(uri, false)).start();

				Toast.makeText(ActivityPublishSelling.this, "ͼƬ������",
						Toast.LENGTH_SHORT).show();
				break;
			case REQUEST_CODE_CAMERA:
				uri = Uri.fromFile(new File(Utile.FilePatchInitialize()
						+ "heading_image_cache.jpg"));
				new Thread(new ImageThread(uri, true)).start();

				Toast.makeText(ActivityPublishSelling.this, "ͼƬ������",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(ActivityPublishSelling.this, "������ȡ��",
						Toast.LENGTH_SHORT).show();
				break;
			}
		} else {
			Toast.makeText(ActivityPublishSelling.this, "������ȡ��",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
		line.setVisibility(View.GONE);
		linearLayout_classify.setTag("1");
		observableGridView.setVisibility(View.GONE);
		cid = entityGoodsClassifies.get(id).getId();
		textView_classify.setText(entityGoodsClassifies.get(id).getName());
	}

	/**
	 * ѡ��ͷ����Դ�Ի���
	 */
	private void ImageSelector() {
		Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("�����Ƭ");
		dialog.setMessage("ѡ����Ƭ��Դ");
		dialog.setNegativeButton("���", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intent, REQUEST_CODE_IMAGE);
				} catch (ActivityNotFoundException e) {
					Toast.makeText(ActivityPublishSelling.this, "ý�������ʧ��",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setPositiveButton("�����", new OnClickListener() {
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
					Toast.makeText(ActivityPublishSelling.this, "���������ʧ��",
							Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
		dialog.show();
	}

	/**
	 * ���ͼƬ �������
	 * 
	 * @author Administrator
	 * 
	 */
	class ImageThread extends Thread {
		private Uri uri = null;
		private boolean isCamera = false;

		public ImageThread(Uri uri, boolean isCamera) {
			this.uri = uri;
			this.isCamera = isCamera;
		}

		public void run() {
			if (uri != null) {
				String root = Utile.FilePatchInitialize();
				if (!root.equals("")) {
					try {
						String fileName = System.currentTimeMillis() / 1000
								+ ".jpg";
						final Bitmap bitmap = saveImage(uri, new File(root
								+ fileName));
						if (bitmap == null) {
							Toast.makeText(ActivityPublishSelling.this,
									"ͼƬ��ȡʧ��", Toast.LENGTH_SHORT).show();
						} else {
							runOnUiThread(new Runnable() {// ���ͼƬ
								public void run() {
									imageButtons_image.get(id).setImageBitmap(
											bitmap);
									imageViews_cancel.get(id).setVisibility(
											View.VISIBLE);
								}
							});
						}
					} catch (IOException e) {
						Toast.makeText(ActivityPublishSelling.this, "ͼƬ��ȡʧ��",
								Toast.LENGTH_SHORT).show();
					} catch (URISyntaxException e) {
						Toast.makeText(ActivityPublishSelling.this, "ͼƬ��ȡʧ��",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ActivityPublishSelling.this, "SD��������",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ActivityPublishSelling.this, "ͼƬ��ȡʧ��",
						Toast.LENGTH_SHORT).show();
			}
		};

		/**
		 * ��ȡ����ͼƬ���ر�����ָ��Ŀ¼
		 * 
		 * @param bitmap
		 *            ԭʼͼƬ
		 * @param file
		 *            �ļ�·��
		 * @return
		 * @throws IOException
		 * @throws URISyntaxException
		 */
		private Bitmap saveImage(Uri uri, File file) throws IOException,
				URISyntaxException {
			Bitmap bitmap = getBitmap(uri);
			if (bitmap != null) {
				// ����ѹ��ͼƬ
				int quality = 100;// �ļ�ѹ������
				int fileSize = 100;// �ļ���С��KB��
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
						byteArrayOutputStream);
				while (byteArrayOutputStream.toByteArray().length / 1024 > fileSize) {
					byteArrayOutputStream.reset();
					bitmap.compress(Bitmap.CompressFormat.JPEG, quality,
							byteArrayOutputStream);
					quality -= 10;
					if (quality <= 0) {
						break;
					}
				}
				// �жϻ����ļ����Ƿ���ڣ��������򴴽�
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				// ��ѹ�����ͼƬд�뵽����
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(byteArrayOutputStream.toByteArray());
				fileOutputStream.flush();
				fileOutputStream.close();
				// ��file��ӵ�������
				fileImages.put(id, file);
			}
			return bitmap;
		}

		/**
		 * uriתbitmap����
		 * 
		 * @param uri
		 *            ����uri
		 * @return bitmap
		 * @throws URISyntaxException
		 */
		private Bitmap getBitmap(Uri uri) throws URISyntaxException {
			Bitmap bitmap = null;
			if (!isCamera) {
				Cursor cursor = getContentResolver().query(uri, null, null,
						null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					// ��ȡͼƬ·��
					String filePath = cursor.getString(cursor
							.getColumnIndex("_data"));
					// ��ȡ��ת�ĽǶ�
					String orientation = cursor.getString(cursor
							.getColumnIndex("orientation"));
					if (filePath != null) {
						bitmap = decodeBitmap(filePath);
						// �ж�ͼƬ�Ƿ����˷�ת
						int angle = 0;
						if (orientation != null && !"".equals(orientation)) {
							angle = Integer.parseInt(orientation);
						}
						if (angle != 0) {
							Matrix matrix = new Matrix();
							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							matrix.setRotate(angle);
							bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
									height, matrix, true);

						}
					}
				}
			} else {
				String filePath = new File(new URI(uri.toString())).toString();
				bitmap = decodeBitmap(filePath);
				Matrix matrix = new Matrix();
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				matrix.setRotate(readPictureDegree(filePath));
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
			}
			return bitmap;
		}

		/**
		 * ��ȡͼƬ����ͼ
		 * 
		 * @param filePath
		 *            ����·��
		 * @return bitmap
		 */
		private Bitmap decodeBitmap(String filePath) {
			// �õ�ͼƬ�ߴ�
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			// �ɱ���������480
			options.inSampleSize = calculateInSampleSize(options, 480,
					options.outHeight * (480 / options.outWidth));
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(filePath, options);
		}

		/**
		 * ��ȡͼƬ���ű���
		 * 
		 * @param options
		 *            ����
		 * @param reqWidth
		 *            ��Ҫ��
		 * @param reqHeight
		 *            ��Ҫ��
		 * @return ���ر���
		 */
		private int calculateInSampleSize(BitmapFactory.Options options,
				int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {

				final int halfHeight = height / 2;
				final int halfWidth = width / 2;
				while ((halfHeight / inSampleSize) > reqHeight
						&& (halfWidth / inSampleSize) > reqWidth) {
					inSampleSize *= 2;
				}
			}
			return inSampleSize;
		}

		/**
		 * ��ȡͼƬ���ԣ���ת�ĽǶ�
		 * 
		 * @param path
		 *            ͼƬ����·��
		 * @return degree��ת�ĽǶ�
		 */
		public int readPictureDegree(String path) {
			int degree = 0;
			try {
				ExifInterface exifInterface = new ExifInterface(path);
				int orientation = exifInterface.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return degree;
		}
	}

	private void setData() {
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_1));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_2));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_3));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_4));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_5));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_6));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_7));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_8));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_9));
		imageButtons_image
				.add((ImageButton) findViewById(R.id.imagebutton_image_manager_10));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_1));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_2));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_3));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_4));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_5));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_6));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_7));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_8));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_9));
		imageViews_cancel
				.add((ImageView) findViewById(R.id.imageview_imagemanager_10));

		imageButtons_image.get(0).post(new Runnable() {
			public void run() {
				for (int i = 0; i < imageButtons_image.size(); i++) {
					LayoutParams layoutParams = imageButtons_image.get(i)
							.getLayoutParams();
					layoutParams.height = imageButtons_image.get(0)
							.getMeasuredWidth();
					imageButtons_image.get(0).setLayoutParams(layoutParams);
				}
			}
		});
		for (int i = 0; i < imageButtons_image.size(); i++) {
			final int j = i;
			imageButtons_image.get(i).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							id = j;
							if (imageViews_cancel.get(j).getVisibility() == View.INVISIBLE) {
								ImageSelector();
							} else {
								imageViews_cancel.get(j).setVisibility(
										View.INVISIBLE);
								imageButtons_image.get(j).setImageResource(
										R.drawable.icon_image_add);
								fileImages.remove(id);
							}
						}
					});
		}
	}
}