package com.example.tomizzje.garagecross.components;

import com.example.tomizzje.garagecross.activities.BaseActivity;
import com.example.tomizzje.garagecross.adapters.ExerciseAdapter;
import com.example.tomizzje.garagecross.adapters.FoodAdapter;
import com.example.tomizzje.garagecross.adapters.FoodGroupAdapter;
import com.example.tomizzje.garagecross.adapters.ShareAdapter;
import com.example.tomizzje.garagecross.modules.ApplicationModule;
import com.example.tomizzje.garagecross.modules.FirebaseDepotModule;
import com.example.tomizzje.garagecross.modules.FirebaseLoginModule;
import com.example.tomizzje.garagecross.modules.FirebaseServerModule;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, FirebaseServerModule.class, FirebaseLoginModule.class, FirebaseDepotModule.class})
public interface BaseComponent {

    /**
     * List of classes where models were injected
     */

    void inject(BaseActivity baseActivity);
    void inject(ExerciseAdapter.ExerciseViewHolder exerciseViewHolder);
    void inject(ShareAdapter.ShareViewHolder shareViewHolder);
    void inject(FoodAdapter.FoodViewHolder foodViewHolder);
    void inject(FoodGroupAdapter.FoodGroupViewHolder foodGroupViewHolder);
}
