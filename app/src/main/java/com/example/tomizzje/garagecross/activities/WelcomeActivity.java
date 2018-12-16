package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.entities.User;
import com.example.tomizzje.garagecross.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WelcomeActivity extends MenuBaseActivity{

    /**
     * Fields connected by the view and strings.xml
     */

    @BindView(R.id.tvWelcome)
    TextView tvWelcome;

    @BindView(R.id.btnGoodDay)
    Button btnGoodDay;

    @BindView(R.id.tvLevel)
    TextView tvLevel;

    @BindView(R.id.pgsBar)
    ProgressBar pgsBar;

    @BindView(R.id.tvProgressInfo)
    TextView tvProgressInfo;

    @BindString(R.string.database_reference_users)
    String usersReference;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.intent_bundle_key_select_exercise)
    String intentExerciseString;

    @BindString(R.string.welcome_text)
    String welcomeText;

    @BindString(R.string.point_text)
    String pointText;

    @BindString(R.string.unknown_error_text)
    String errorToast;

    private User user;

    private Exercise exercise;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(firebaseLogin.isSignedIn()) {
            initProgressBar();
            String email = firebaseLogin.getEmail();
            String name = firebaseLogin.getName();
            String userId = firebaseLogin.getCurrentUser();

            this.user = new User(userId, email, name);

            initFindUser();
            initExercise();
            initButtonDay();

        }
    }

    /**
     * Initialize GoodDayButton
     */
    private void initButtonDay() {
        btnGoodDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(exercise != null){
                    Intent intent = new Intent(WelcomeActivity.this, TimerActivity.class);
                    intent.putExtra(intentExerciseString, exercise);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    /**
     * query the exercises from the database to a list for the GoodDayButton
     */
    private void initExercise() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    List<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Exercise.class) != null){
                            exercises.add(snapshot.getValue(Exercise.class));
                        }
                    }
                    exercise = getRandomExercise(exercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
            }
        };
        firebaseServer.findItemsOfNode(valueEventListener, exercisesReference);
    }

    /**
     * Query the users from the database and save it if it is the first time the user logged in
     */
    private void initFindUser() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() ) {
                    List<User> users = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(User.class) != null){
                            users.add(snapshot.getValue(User.class));
                        }

                    }

                    boolean gotIt = false;
                    for(User u : users) {
                        if(u.getUser_id().equals(user.getUser_id())) {
                            gotIt = true;
                            user = u;
                        }
                    }
                    if(!gotIt) {
                        firebaseServer.insertEntity(user, usersReference);
                    }

                    String name = Utils.fixDisplayName(user.getName());
                    String welcomeMsg = welcomeText + name;
                    tvWelcome.setText(welcomeMsg);


                    String levelMsg = Difficulty.getDifficultyLevelByExperience(user.getExperience()).toString() + ": " + user.getExperience() + pointText;
                    tvLevel.setText(levelMsg);

                    String progressInfo = user.getExperience() + "/" + pgsBar.getMax();
                    tvProgressInfo.setText(progressInfo);
                    pgsBar.setProgress(user.getExperience());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
            }
        };
        firebaseServer.findItemsOfNode(valueEventListener, usersReference);
    }



    @Override
    protected void onPause() {
        super.onPause();
        firebaseLogin.detachListener();
    }

    /**
     * Set progressbar limits
     */
    private void initProgressBar() {
        pgsBar.setMax(300);
        pgsBar.setProgress(0);
    }

    /**
     * Choose a random exercise from a list
     * @param exercises list
     * @return a random Exercise
     */
    private Exercise getRandomExercise(List<Exercise> exercises ) {
        Random random = new Random();
        return exercises.get(random.nextInt(exercises.size()));
    }
}
