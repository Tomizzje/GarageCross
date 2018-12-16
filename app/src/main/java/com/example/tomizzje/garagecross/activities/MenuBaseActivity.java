package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This method implements the menu items and their function
     * @param item of the menu
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.all_exercise_menu:
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
                                getBackToIndex();
                            }
                        });
                firebaseLogin.detachListener();
                return true;
            case R.id.doneExercise_menu:
                Intent intentToDoneExercise = new Intent(this, DoneExerciseListActivity.class);
                startActivity(intentToDoneExercise);
                return true;

            case R.id.diary_menu:
                Intent intentToDiary = new Intent(this, WeightLiftingDiaryActivity.class);
                startActivity(intentToDiary);
                return true;
            case R.id.personalExercise_menu:
                Intent intentToPersonalExercise = new Intent(this, PersonalExerciseListActivity.class);
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
            case R.id.share_list_menu:
                Intent intentToShareList = new Intent(this, ShareListActivity.class);
                startActivity(intentToShareList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * this method navigates back to the welcomeActivity after sign out.
     */
    private void getBackToIndex(){
        Toast.makeText(this, "Sikeres kijelentkezés", Toast.LENGTH_LONG).show();
        Intent intentToWelcomeBack = new Intent(this, WelcomeActivity.class);
        startActivity(intentToWelcomeBack);
        finish();
    }
}
