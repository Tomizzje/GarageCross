package com.example.tomizzje.garagecross.activities;

import android.app.Activity;
import android.app.ActivityManager;
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
import com.example.tomizzje.garagecross.utils.VibrationUtil;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected FirebaseServer firebaseServer;

    @Inject
    protected FirebaseLogin firebaseLogin;

    @Inject
    protected FirebaseDepot firebaseDepot;

    @Inject
    protected VibrationUtil vibrationUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().getBaseComponent().inject(this);

        //CSAK PORTRAIT MODE
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isConnectingToInternet()){
            Toast.makeText(this, "Nincs internet, az adatok nem frissültek", Toast.LENGTH_LONG).show();
            getBackToWelcome();
        }else {
            firebaseLogin.detachListener();
            firebaseLogin.checkLogin(this);
            firebaseLogin.attachListener();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseLogin.detachListener();
    }

    public boolean isConnectingToInternet() {
        if (networkConnectivity()) {
            try {
                Process p1 = Runtime.getRuntime().exec(
                        "ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                boolean reachable = (returnVal == 0);
                if (reachable) {
                    System.out.println("Internet access");
                    return reachable;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        } else
            return false;

    }

    private boolean networkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void getBackToWelcome(){

        if(!(this.getClass().getSimpleName().equals("WelcomeActivity"))){
            Intent intentToWelcomeBack = new Intent(this, WelcomeActivity.class);
            startActivity(intentToWelcomeBack);
        }

    }

}
