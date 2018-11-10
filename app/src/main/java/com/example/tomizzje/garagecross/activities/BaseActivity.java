package com.example.tomizzje.garagecross.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

}
