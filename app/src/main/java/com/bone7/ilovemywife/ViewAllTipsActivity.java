package com.bone7.ilovemywife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewAllTipsActivity extends AppCompatActivity {

    WebView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_tips);
        content = (WebView)findViewById(R.id.webView);
        content.loadUrl("file:///android_asset/Tips/tips.html");
    }
}
