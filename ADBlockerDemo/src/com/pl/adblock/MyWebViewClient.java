package com.pl.adblock;
import com.pl.adblocker.ADBlocker;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MyWebViewClient extends WebViewClient
{
	private String originUrl;
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO 自动生成的方法存根
		originUrl=url;
		super.onPageStarted(view, url, favicon);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		// TODO 自动生成的方法存根
		if (ADBlocker.getInstance().matches(url, null, originUrl, null)) {
			Log.d("AdBlockNew", "blocked:"+url);
			return new WebResourceResponse(null, null, null);
		}else{
			Log.d("AdBlockNew", "unblocked:"+url);
		}
		return super.shouldInterceptRequest(view, url);
	}
	
	
}