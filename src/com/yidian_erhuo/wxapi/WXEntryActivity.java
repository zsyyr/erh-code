package com.yidian_erhuo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends Activity {
	private static WXRequest wxRequest;

	public static WXRequest getWxRequest() {
		return wxRequest;
	}

	public static void setWxRequest(WXRequest wxRequest) {
		WXEntryActivity.wxRequest = wxRequest;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (null != wxRequest) {
			wxRequest.request(getIntent());
		}
		finish();
	}

	public interface WXRequest {
		void request(Intent intent);
	}
}