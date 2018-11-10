package com.example.tomizzje.garagecross.modules;

import android.content.Context;

import com.example.tomizzje.garagecross.models.FirebaseLogin;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseLoginModule {

    @Provides
    @Singleton

    FirebaseLogin providesFirebaseLogin(Context context) {
        return new FirebaseLogin();
    }


}
