package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.tomizzje.garagecross.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public abstract class MenuBaseActivity extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.exercise_menu:
                Intent intentToExerciseList = new Intent(this, ExerciseListActivity.class);
                startActivity(intentToExerciseList);
                return true;
            case R.id.welcome_menu:
                Intent intentToWelcome = new Intent(this, WelcomeActivity.class);
                startActivity(intentToWelcome);
                return true;
            case R.id.logout_menu:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                getBackToWelcome();
                                Log.d("HEYHOKA", "User logged out");
                                //firebaseLogin.attachListener();
                                //Log.d("HEYHOKA", "User attachedlistener out");
                            }
                        });
                firebaseLogin.detachListener();
                Log.d("HEYHOKA", "User detachedlistener out");



                return true;
            case R.id.doneExercise_menu:
                Intent intentToDoneExercise = new Intent(this, DoneExerciseListActivity.class);
                startActivity(intentToDoneExercise);
                return true;

            case R.id.diary_menu:
                Intent intentToDiary = new Intent(this, WeightLiftingDiaryActivity.class);
                startActivity(intentToDiary);
                return true;
            case R.id.users_menu:
                Intent intentToUsers = new Intent(this, UserListActivity.class);
                startActivity(intentToUsers);
                return true;
            case R.id.insert_menu:
                Intent intentToInsertExercise = new Intent(this, InsertExerciseActivity.class);
                startActivity(intentToInsertExercise);
                return true;
            case R.id.personalExercise_menu:
                Intent intentToPersonalExercise = new Intent(this, PersonalExerciseListActivty.class);
                startActivity(intentToPersonalExercise);
                return true;
            case R.id.favoriteExercise_menu:
                Intent intentToFavoriteExercise = new Intent(this, FavoriteExerciseListActivity.class);
                startActivity(intentToFavoriteExercise);
                return true;
            case R.id.recommendedExercise_menu:
                Intent intentToRecommendedExercise = new Intent(this, RecommendedExerciseListActivity.class);
                startActivity(intentToRecommendedExercise);
                return true;
            case R.id.nutrition_menu:
                Intent intentToNutrition = new Intent(this, NutritionActivity.class);
                startActivity(intentToNutrition);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBackToWelcome(){
        Intent intentToWelcomeBack = new Intent(this, WelcomeActivity.class);
        startActivity(intentToWelcomeBack);
    }
}
