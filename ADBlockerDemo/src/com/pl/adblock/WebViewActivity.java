package com.pl.adblock;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

public class WebViewActivity extends Activity{

	private EditText addressEditText;
	private WebView mWebView;
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
//		ProxySetting2.setProxy(this.getApplicationContext(), "localhost", 2020);
		addressEditText=(EditText) findViewById(R.id.address);
		mWebView=(WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebChromeClient(new WebChromeClient());
//		WebView.setWebContentsDebuggingEnabled(true);
		addressEditText.setOnKeyListener(new OnKeyListener() {			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO 自动生成的方法存根
				if (keyCode==KeyEvent.KEYCODE_ENTER) {
					String url=addressEditText.getText().toString();
					if (!url.startsWith("http://")) {
						url="http://"+url;
					}
					mWebView.loadUrl(url);
					addressEditText.clearFocus();
					InputMethodManager ipm=(InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
					if(ipm.isActive()){  						  
						ipm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0 );  						  
						} 
					return true;
				}
				return false;
			}
		} );
		
	}

}

