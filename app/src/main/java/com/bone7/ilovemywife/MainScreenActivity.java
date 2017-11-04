package com.bone7.ilovemywife;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kila.apprater_dialog.lars.AppRater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainScreenActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Test ads are being shown. "
            + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID.";

    private static final int START_LEVEL = 1;
    private int mLevel;
    private FancyButton btnCalendar, btnSetting, btnViewTips;
    private InterstitialAd mInterstitialAd;
    public static String myConfig="";
    public static MyConfigClass appConfig;
    Intent intent;
    Gson gson = new Gson();
//    private TextView mLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //App rate dialog

        if(savedInstanceState != null) {
            new AppRater.StarBuilder(this, "com.bone7.ilovemywife")
                    .showDefault()
                    .minimumNumberOfStars(4)
                    .email("akiba1989@gmail.com")
                    .timesToLaunch(5)
                    .daysToWait(1)
                    .timesToLaunchInterval(2)
                    .appLaunched();
        }
//        setContentView(R.layout.activity_main_screen);
        setContentView(R.layout.new_main_layout_2);

        // Create the next level button, which tries to show an interstitial when clicked.
//        btnCalendar = ((Button) findViewById(R.id.next_level_button));
        btnCalendar = ((FancyButton) findViewById(R.id.btnCalendar));
        btnCalendar.setEnabled(false);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), CalendarScreenActivity.class);
                showInterstitial();
            }
        });
        btnSetting =(FancyButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), SettingScreenActivity.class);
//                intent.putExtra("config",myConfig);
                intent.putExtra("config",gson.toJson(appConfig));
                showInterstitial();
            }
        });
        btnViewTips = (FancyButton) findViewById(R.id.btnViewTips);


        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();

        //Check config
        myConfig = MyAndroidHelper.readFromFile("config", getApplicationContext());
        if(myConfig.length() < 2)
        {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(R.string.dialog_firsttime_title)
                    .content(R.string.dialog_firsttime_content)
                    .positiveText("Agree")
                    .negativeText("No, thanks");
            final MaterialDialog dialog = builder.build();
            View positive = dialog.getActionButton(DialogAction.POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    intent = new Intent(getApplicationContext(), SettingScreenActivity.class);
                    intent.putExtra("config",myConfig);
                    startActivity(intent);
                }
            });
            View negative = dialog.getActionButton(DialogAction.NEGATIVE);
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appConfig = new MyConfigClass();
                    appConfig.notification = true;
                    appConfig.eventList = new ArrayList<>();
                }
            });
            dialog.show();
        }
        else
        {
            Type listType = new TypeToken<MyConfigClass>(){}.getType();
            appConfig = (MyConfigClass) gson.fromJson(myConfig, listType);
        }

        // Fetch next event
        fetchNextEvent();
    }
    public void fetchNextEvent()
    {

        if(appConfig.eventList.size() == 0)
            return;
        else {
            TextView header_day, header_yearmonth, header_event,header_day1, header_yearmonth1, header_event1;
            header_day = (TextView) findViewById(R.id.header_day);
            header_yearmonth = (TextView) findViewById(R.id.header_year_month);
            header_event = (TextView) findViewById(R.id.txt_next_event);
            header_day1 = (TextView) findViewById(R.id.header_day1);
            header_yearmonth1 = (TextView) findViewById(R.id.header_year_month1);
            header_event1 = (TextView) findViewById(R.id.txt_next_event1);
            Calendar calendar = Calendar.getInstance();
            if (appConfig.eventList.size() == 1) {
                header_day.setText(appConfig.eventList.get(0).eventDate.substring(8,10));
                header_yearmonth.setText(appConfig.eventList.get(0).eventDate.substring(5,7));
                header_event.setText(appConfig.eventList.get(0).eventName);
            }
            else
            {
                MyConfigClass.MyEvent event1,event2;
                String today = CalendarScreenActivity.TREEMAP_FORMAT.format(calendar.getTime());
                int index = 0;
                for(int i = 0;i<appConfig.eventList.size();i++)
                {
                    if(appConfig.eventList.get(i).eventDate.substring(5,10).compareTo(today.substring(5,10)) > 0)
                    {
                        index = i;
                        break;
                    }
                }
                event1 = appConfig.eventList.get(index);
                event2 = appConfig.eventList.get((index+1)%appConfig.eventList.size());
                header_day.setText(event1.eventDate.substring(8,10));
                header_yearmonth.setText(event1.eventDate.substring(5,7));
                header_event.setText(event1.eventName);

                header_day1.setText(event2.eventDate.substring(8,10));
                header_yearmonth1.setText(event2.eventDate.substring(5,7));
                header_event1.setText(event2.eventName);
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                btnCalendar.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                btnCalendar.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        btnCalendar.setEnabled(false);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
//        mLevelTextView.setText("Level " + (++mLevel));
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
        //intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    protected void onResume() {

        super.onResume();
        fetchNextEvent();
//        myConfig = MyAndroidHelper.readFromFile("config", getApplicationContext());
//        Log.i("myconfigMain",myConfig);

    }
}
