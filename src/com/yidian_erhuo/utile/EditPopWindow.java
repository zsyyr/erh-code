package com.yidian_erhuo.utile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.yidian_erhuo.R;

public class EditPopWindow extends PopupWindow implements OnTouchListener {
	private Context context;
	private View contentView;
	private LinearLayout linearLayout_background, linearLayout_view;

	@SuppressLint("ClickableViewAccessibility")
	public EditPopWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		this.context = context;
		initView(context, itemsOnClick);
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
	private void initView(Context context, OnClickListener itemsOnClick) {
		contentView = LinearLayout.inflate(context, R.layout.view_edit, null);
		((Button) contentView.findViewById(R.id.button_edit_delete))
				.setOnClickListener(itemsOnClick);
		((Button) contentView.findViewById(R.id.button_edit_edit))
				.setOnClickListener(itemsOnClick);
		((Button) contentView.findViewById(R.id.button_edit_cancle))
				.setOnTouchListener(this);
		linearLayout_view = (LinearLayout) contentView
				.findViewById(R.id.linearlayout_edit_view);
		linearLayout_background = (LinearLayout) contentView
				.findViewById(R.id.linearlayout_edit_background);
		linearLayout_background.setOnTouchListener(this);
	}

	@Override
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouch(View v, MotionEvent event) {
		dismissAnimation();
		return true;
	}

	@Override
	@SuppressWarnings("static-access")
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		linearLayout_view.setVisibility(View.INVISIBLE);
		Animation animation_in = new AnimationUtils().loadAnimation(context,
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
						.loadAnimation(context, R.anim.anim_share_in));
			}
		});
	}

	@SuppressWarnings("static-access")
	private void dismissAnimation() {
		Animation animation_out = new AnimationUtils().loadAnimation(context,
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
						.loadAnimation(context, R.anim.anim_share_bg_out));
			}
		});
		new Handler().postDelayed(new Runnable() {
			public void run() {
				dismiss();
			}
		}, 400);
	}
}