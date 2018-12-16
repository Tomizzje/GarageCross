package com.example.tomizzje.garagecross.application;

import android.app.Application;

import com.example.tomizzje.garagecross.components.BaseComponent;
import com.example.tomizzje.garagecross.components.DaggerBaseComponent;
import com.example.tomizzje.garagecross.modules.ApplicationModule;
import com.example.tomizzje.garagecross.modules.FirebaseDepotModule;
import com.example.tomizzje.garagecross.modules.FirebaseLoginModule;
import com.example.tomizzje.garagecross.modules.FirebaseServerModule;


import lombok.Getter;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Getter
    private BaseComponent baseComponent;

    public BaseApplication() {
        instance = this;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * add modules to the baseApplicaton
         */
        baseComponent = DaggerBaseComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .firebaseServerModule(new FirebaseServerModule())
                .firebaseDepotModule(new FirebaseDepotModule())
                .firebaseLoginModule(new FirebaseLoginModule())
                .build();
    }
}
