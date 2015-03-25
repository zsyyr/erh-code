package com.yidian_erhuo.utile;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yidian_erhuo.R;

public class TitleView {
	private FrameLayout framelayout_titleview;
	private TextView textView_left, textView_mid, textView_right;
	private ImageButton imageButton_left, imageButton_mid, imageButton_right;

	public TitleView(Activity activity) {
		framelayout_titleview = (FrameLayout) activity
				.findViewById(R.id.framelayout_titleview);
		textView_left = (TextView) activity
				.findViewById(R.id.textview_title_left);
		textView_mid = (TextView) activity
				.findViewById(R.id.textview_title_mid);
		textView_right = (TextView) activity
				.findViewById(R.id.textview_title_right);
		imageButton_left = (ImageButton) activity
				.findViewById(R.id.imagebutton_title_left);
		imageButton_mid = (ImageButton) activity
				.findViewById(R.id.imagebutton_title_mid);
		imageButton_right = (ImageButton) activity
				.findViewById(R.id.imagebutton_title_right);
	}

	public void hide() {
		framelayout_titleview.setVisibility(View.GONE);
	}

	public void show() {
		framelayout_titleview.setVisibility(View.VISIBLE);
	}

	public TextView getTextView_left() {
		return textView_left;
	}

	public void setTextView_left(TextView textView_left) {
		this.textView_left = textView_left;
	}

	public TextView getTextView_mid() {
		return textView_mid;
	}

	public void setTextView_mid(TextView textView_mid) {
		this.textView_mid = textView_mid;
	}

	public TextView getTextView_right() {
		return textView_right;
	}

	public void setTextView_right(TextView textView_right) {
		this.textView_right = textView_right;
	}

	public ImageButton getImageButton_left() {
		return imageButton_left;
	}

	public void setImageButton_left(ImageButton imageButton_left) {
		this.imageButton_left = imageButton_left;
	}

	public ImageButton getImageButton_mid() {
		return imageButton_mid;
	}

	public void setImageButton_mid(ImageButton imageButton_mid) {
		this.imageButton_mid = imageButton_mid;
	}

	public ImageButton getImageButton_right() {
		return imageButton_right;
	}

	public void setImageButton_right(ImageButton imageButton_right) {
		this.imageButton_right = imageButton_right;
	}

	public ImageButton LeftButton(int res, int background) {
		if (imageButton_left != null) {
			imageButton_left.setImageResource(res);
			imageButton_left.setVisibility(View.VISIBLE);
			if (background == 0) {
				imageButton_left.setBackgroundColor(Color.TRANSPARENT);
			} else {
				imageButton_left.setBackgroundResource(background);
			}
			imageButton_left.setVisibility(View.VISIBLE);
		}
		return imageButton_left;
	}

	public ImageButton MidButton(int res, int background) {
		if (imageButton_mid != null) {
			imageButton_mid.setImageResource(res);
			imageButton_mid.setVisibility(View.VISIBLE);
			if (background == 0) {
				imageButton_mid.setBackgroundColor(Color.TRANSPARENT);
			} else {
				imageButton_mid.setBackgroundResource(background);
			}
			imageButton_mid.setVisibility(View.VISIBLE);
		}
		return imageButton_mid;
	}

	public ImageButton RightButton(int res, int background) {
		if (imageButton_right != null) {
			imageButton_right.setImageResource(res);
			imageButton_right.setVisibility(View.VISIBLE);
			if (background == 0) {
				imageButton_right.setBackgroundColor(Color.TRANSPARENT);
			} else {
				imageButton_right.setBackgroundResource(background);
			}
			imageButton_right.setVisibility(View.VISIBLE);
		}
		return imageButton_right;
	}

	public TextView LeftTextView(String msg, int background) {
		if (textView_left != null) {
			textView_left.setText(msg);
			textView_left.setVisibility(View.VISIBLE);
			if (background != 0) {
				textView_left.setBackgroundResource(background);
			}
		}
		return textView_left;
	}

	public TextView MidTextView(String msg, int background) {
		if (textView_mid != null) {
			textView_mid.setText(msg);
			textView_mid.setVisibility(View.VISIBLE);
			if (background != 0) {
				textView_mid.setBackgroundResource(background);
			}
		}
		return textView_mid;
	}

	public TextView RightTextView(String msg, int background) {
		if (textView_right != null) {
			textView_right.setText(msg);
			textView_right.setVisibility(View.VISIBLE);
			if (background != 0) {
				textView_right.setBackgroundResource(background);
			}
		}
		return textView_right;
	}
}