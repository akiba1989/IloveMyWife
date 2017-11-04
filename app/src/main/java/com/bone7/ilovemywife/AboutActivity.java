package com.bone7.ilovemywife;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import mehdi.sakout.fancybuttons.FancyButton;

public class AboutActivity extends AppCompatActivity {

    FancyButton btnPrivacy, btnFb, btnTwitter, btnGp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView webview = (WebView)findViewById(R.id.webviewInfor);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/about.html");
        btnPrivacy = (FancyButton) findViewById(R.id.btnPrivacy);
        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // https://blackfishapp.wordpress.com/privacy/
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://blackfishapp.wordpress.com/privacy"));
                startActivity(intent);
            }
        });

        btnFb = (FancyButton) findViewById(R.id.btnShare);
        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://play.google.com/store/apps/details?id=bone7.nyanyaninja";
                try {
                    Intent intent1 = new Intent();
//                    intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
                    intent1.setAction("android.intent.action.SEND");
                    intent1.setType("text/plain");
                    intent1.putExtra("android.intent.extra.TEXT", url);
                    startActivity(intent1);
                } catch (Exception e) {
                    // If we failed (not native FB app installed), try share through SEND
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + url;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
