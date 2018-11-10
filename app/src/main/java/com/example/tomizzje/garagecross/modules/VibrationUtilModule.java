package com.example.tomizzje.garagecross.modules;

import android.content.Context;

import com.example.tomizzje.garagecross.utils.VibrationUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ApplicationModule.class)
public class VibrationUtilModule {

    @Provides
    @Singleton
    VibrationUtil provideVibrationUtil(Context context) {
       return new VibrationUtil(context);
    }
}
