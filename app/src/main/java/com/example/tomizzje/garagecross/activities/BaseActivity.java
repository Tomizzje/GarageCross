package com.example.tomizzje.garagecross.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.models.FirebaseDepot;
import com.example.tomizzje.garagecross.models.FirebaseLogin;
import com.example.tomizzje.garagecross.models.FirebaseServer;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected FirebaseServer firebaseServer;

    @Inject
    protected FirebaseLogin firebaseLogin;

    @Inject
    protected FirebaseDepot firebaseDepot;

    ConnectivityManager connectivityManager;
    boolean connected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().getBaseComponent().inject(this); // Needed by Dagger Injection
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Set orientation to Portrait mode only
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isOnline() ){
            firebaseLogin.detachListener();
            firebaseLogin.checkLogin(this);
            firebaseLogin.attachListener();
        }else {
            Toast.makeText(this, "Nincs internet, az adatok nem frissültek" , Toast.LENGTH_LONG).show();
            getBackToWelcome();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseLogin.detachListener();
    }

    /**
        * Navigates back to WelcomeActivity
        * no param
     */
    public void getBackToWelcome(){

        if(!(this.getClass().getSimpleName().equals("WelcomeActivity"))){
            Intent intentToWelcomeBack = new Intent(this, WelcomeActivity.class);
            startActivity(intentToWelcomeBack);
            finish();
        }
    }

    /**
     * Checks if the device is connected to a Network.
     * no param
     * @return boolean
     */

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            assert connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }

}
