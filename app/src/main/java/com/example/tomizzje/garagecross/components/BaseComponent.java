package com.example.tomizzje.garagecross.components;

import com.example.tomizzje.garagecross.activities.BaseActivity;
import com.example.tomizzje.garagecross.adapters.ExerciseAdapter;
import com.example.tomizzje.garagecross.modules.ApplicationModule;
import com.example.tomizzje.garagecross.modules.FirebaseDepotModule;
import com.example.tomizzje.garagecross.modules.FirebaseLoginModule;
import com.example.tomizzje.garagecross.modules.FirebaseServerModule;
import com.example.tomizzje.garagecross.modules.VibrationUtilModule;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, VibrationUtilModule.class, FirebaseServerModule.class, FirebaseLoginModule.class, FirebaseDepotModule.class})
public interface BaseComponent {

    /*void inject(TimerActivity timerActivity);
    void inject (ExerciseListActivity exerciseListActivity);
    void inject (DoneExerciseListActivity doneExerciseListActivity);
    void inject (InsertExerciseActivity insertExerciseActivity);
    void inject (WeightLiftingDiaryActivity diaryActivity);
    void inject (InsertRecordActivity insertRecordActivity);
    void inject (WelcomeActivity welcomeActivity);
    void inject (UserListActivity userListActivity);


    void inject (PersonalExerciseListActivty personalExerciseListActivty);
    void inject (FavoriteExerciseListActivity favoriteExerciseListActivity);*/
    void inject(BaseActivity baseActivity);
    void inject (ExerciseAdapter.ExerciseViewHolder exerciseViewHolder);
}
