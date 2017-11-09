package com.bone7.ilovemywife;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;

import org.sufficientlysecure.donations.DonationsFragment;

public class DonationActivity extends FragmentActivity {

    /**
     * Google
     */
    private static final String GOOGLE_PUBKEY = "AB";
    private static final String[] GOOGLE_CATALOG = new String[]{"ilovemywife.donation.2",
            "ilovemywife.donation.5", "ilovemywife.donation.10", "ilovemywife.donation.20", "ilovemywife.donation.50",
            "ilovemywife.donation.100"};
    private static final String[] DONATE_CATALOG = new String[]{"2 USD", "5 USD", "10 USD", "20 USD", "50 USD", "100 USD"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_donation);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DonationsFragment donationsFragment;
//        donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
//                    getResources().getStringArray(R.array.donation_google_catalog_values), false, null, null,
//                    null, false, null, null, false, null);
        donationsFragment = DonationsFragment.newInstance(false, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
                DONATE_CATALOG, false, null, null,
                null, false, null, null, false, null);


        ft.replace(R.id.donations_activity_container, donationsFragment, "donationsFragment");
        ft.commit();
    }

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("donationsFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            Gson gson = new Gson();
            MainScreenActivity.appConfig.enableAds = false;
            MyAndroidHelper.writeToFile("config", gson.toJson(MainScreenActivity.appConfig),DonationActivity.this);
        }
    }

}