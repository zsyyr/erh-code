package com.yidian_erhuo.utile;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ObservableGridView extends GridView {
	public ObservableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableGridView(Context context) {
		super(context);
	}

	public ObservableGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}