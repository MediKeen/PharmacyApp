package com.medikeen.pharmacy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.medikeen.pharmacy.notifications.QuickstartPreferences;
import com.medikeen.pharmacy.notifications.RegistrationIntentService;
import com.medikeen.pharmacy.utils.SessionManager;


public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    String sessionId;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(SplashActivity.this);
        sessionId = sessionManager.getUserDetails().getPharmacyUserSessionId();
        progressBar = (ProgressBar) findViewById(R.id.splashProgress);

        Log.e("SESSION ID: ", "SESSION ID: " + sessionId);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                progressBar.setVisibility(View.GONE);
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    if (sessionManager.isLoggedIn()) {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "" + getString(R.string.token_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (sessionId.isEmpty()) {
            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
