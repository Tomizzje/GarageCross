package com.example.tomizzje.garagecross.modules;

import android.content.Context;

import com.example.tomizzje.garagecross.models.FirebaseServer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseServerModule {

    @Provides
    @Singleton
    FirebaseServer providesFirebaseServer(Context context) {
        return new FirebaseServer();
    }
}
