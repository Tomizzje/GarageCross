package com.example.tomizzje.garagecross.modules;

import android.content.Context;

import com.example.tomizzje.garagecross.models.FirebaseDepot;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseDepotModule {

    @Provides
    @Singleton
    FirebaseDepot providesFirebaseDepot(Context context) {
        return new FirebaseDepot();
    }
}
