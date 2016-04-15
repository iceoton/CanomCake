package com.iceoton.canomcake.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.iceoton.canomcake.R;
import com.iceoton.canomcake.fragment.MainFragment;
import com.iceoton.canomcake.service.GcmRegisterService;


public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver();
        if (checkPlayServices()) {
            registerGcm();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    public void placeFragmentToContrainer(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    // Get back press work only at second press and notify user to press again
    // to exit.
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("DEBUG", "popping BackStack");
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed(); // Exit
            } else {
                Toast.makeText(getBaseContext(), R.string.press_one_again,
                        Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    private void registerGcm() {
        Intent intent = new Intent(this, GcmRegisterService.class);
        startService(intent);
        Log.d("DEBUG", "Register GCM");
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(GcmRegisterService.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    private BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences.getBoolean(GcmRegisterService.SENT_TOKEN_TO_SERVER, false);
            // TODO Do something here
            if(sentToken){
                Log.d("DEBUG", "Sent token to server");
            }
        }
    };

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("DEBUG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
