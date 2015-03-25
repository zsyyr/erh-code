package com.yidian_erhuo.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebView;

import com.yidian_erhuo.R;
import com.yidian_erhuo.utile.TitleView;

@SuppressLint("SetJavaScriptEnabled")
public class ActivityAbout extends ActivityBase {
	private String url = "http://123.57.132.230/about.html";

	private WebView webView;
	private TitleView titleView;

	@Override
	public void initView() {
		setContentView(R.layout.activity_about);

		titleView = new TitleView(this);
		titleView.MidTextView("¹ØÓÚ", 0);
		titleView.LeftButton(R.drawable.icon_arrow_left, 0);

		webView = (WebView) findViewById(R.id.webview_about);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);

		titleView.getImageButton_left().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imagebutton_title_left:
			finish();
			break;
		}
	}
}