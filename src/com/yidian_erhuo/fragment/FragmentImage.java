package com.yidian_erhuo.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yidian_erhuo.R;
import com.yidian_erhuo.activity.ActivityGoodsInfo;
import com.yidian_erhuo.activity.ActivityImageGallery;
import com.yidian_erhuo.entity.EntityImage;
import com.yidian_erhuo.utile.Utile;

@SuppressLint({ "ClickableViewAccessibility", "FloatMath" })
public class FragmentImage extends FragmentBase {

	private boolean isExit = false;
	private int id = 0;
	private int mode = NONE;
	private String url = "";
	private String sid = "";
	private float oldDist = 1f;
	private boolean Scale = false;
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private PointF mid = new PointF();
	private PointF start = new PointF();
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();

	private ImageView imageView;
	private ArrayList<EntityImage> entityImages;

	/**
	 * ��Ʒ���鹹��
	 * 
	 * @param entityImages
	 *            ͼƬ�б�
	 * @param id
	 *            ��ǰͼƬ����λ��
	 */
	public FragmentImage(ArrayList<EntityImage> entityImages, int id) {
		this.id = id;
		this.entityImages = entityImages;
		url = Utile.HTTP_IMAGE_URL + entityImages.get(id).getImage();
	}

	/**
	 * ����ͼ
	 * 
	 * @param url
	 *            ͼƬ��ַ
	 * @param sid
	 *            ��Ʒid
	 */
	public FragmentImage(String url, String sid) {
		this.url = url;
		this.sid = sid;
	}

	/**
	 * ��ͼ���
	 * 
	 * @param url
	 *            ͼƬ��ַ
	 * @param Scale
	 *            �Ƿ�����ֶ����ű��
	 */
	public FragmentImage(String url, boolean Scale) {
		this.url = url;
		this.Scale = Scale;
	}

	@Override
	public void initView() {
		setContentView(R.layout.fragment_image);
		// �ؼ�ʵ����
		imageView = (ImageView) contentView
				.findViewById(R.id.imageview_fragment);
		// ͼƬ��������
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.cacheOnDisk(true).cacheInMemory(true)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		// ����ͼƬ
		ImageLoader.getInstance().displayImage(url, imageView,
				displayImageOptions);
		// �ж�ͼƬ�Ƿ��������������
		if (!Scale) {
			// ͼƬ����¼�
			imageView.setClickable(true);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if ("".equals(sid)) {// û����Ʒid����Ϊ����ҳ
						Intent intent = new Intent(getActivity(),
								ActivityImageGallery.class);
						intent.putExtra("id", id);
						intent.putExtra("entityImages", entityImages);
						startActivity(intent);
					} else {// ����Ʒid����Ҫ��ת
						Intent intent = new Intent(getActivity(),
								ActivityGoodsInfo.class);
						intent.putExtra("sid", sid);
						startActivity(intent);
					}
				}
			});
		} else {// ��дontouch�¼�����ɶ��������ŵ���д
			imageView.setScaleType(ScaleType.CENTER);
			// ���Ų�����ȫ��
			imageView.post(new Runnable() {
				public void run() {
					LayoutParams layoutParams = imageView.getLayoutParams();
					layoutParams.width = getActivity().getWindowManager()
							.getDefaultDisplay().getWidth();
					layoutParams.height = getActivity().getWindowManager()
							.getDefaultDisplay().getHeight();
					imageView.setLayoutParams(layoutParams);
				}
			});
			imageView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getActionMasked()) {
					case MotionEvent.ACTION_DOWN:
						matrix.set(imageView.getImageMatrix());
						savedMatrix.set(matrix);
						start.set(event.getX(), event.getY());
						mode = DRAG;
						isExit = true;
						break;
					case MotionEvent.ACTION_POINTER_DOWN:
						oldDist = spacing(event);
						if (oldDist > 10f) {
							savedMatrix.set(matrix);
							midPoint(mid, event);
							mode = ZOOM;
						}
						break;
					case MotionEvent.ACTION_POINTER_UP:
						mode = NONE;
						break;
					case MotionEvent.ACTION_UP:
						if (isExit) {
							getActivity().finish();
						}
						break;
					case MotionEvent.ACTION_MOVE:
						// if (mode == DRAG) {
						// matrix.set(savedMatrix);
						// matrix.postTranslate(event.getX() - start.x,
						// event.getY() - start.y);
						// } else
						if (mode == ZOOM) {
							isExit = false;
							float newDist = spacing(event);
							if (newDist > 10) {
								matrix.set(savedMatrix);
								float scale = newDist / oldDist;
								matrix.postScale(scale, scale, mid.x, mid.y);
							}
						}
						break;
					}
					if (imageView.getScaleType() != ScaleType.MATRIX) {
						imageView.setScaleType(ScaleType.MATRIX);
					}
					imageView.setImageMatrix(matrix);
					return true;
				}
			});
		}
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}