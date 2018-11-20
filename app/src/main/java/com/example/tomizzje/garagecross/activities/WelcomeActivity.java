package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.entities.User;
import com.example.tomizzje.garagecross.utils.ExerciseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WelcomeActivity extends MenuBaseActivity{

    @BindView(R.id.tvWelcome)
    TextView tvWelcome;

    @BindView(R.id.btnGoodDay)
    Button btnGoodDay;

    @BindView(R.id.tvLevel)
    TextView tvLevel;

    @BindView(R.id.pgsBar)
    ProgressBar pgsBar;

    @BindString(R.string.database_reference_users)
    String usersReference;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.intent_bundle_key_select_exercise)
    String intentExerciseString;

    private User user;

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
            initButtonGoodDay();
        }

    }

    private void initFindUser() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() ) {
                    List<User> users = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        users.add(snapshot.getValue(User.class));
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

                    String welcomeMsg = "Üdvözöllek \n " + user.getName();
                    tvWelcome.setText(welcomeMsg);

                    String levelMsg = "A jelenlegi szinted : \n" +  Difficulty.getDifficultyLevelByExperience(user.getExperience()).toString() + ", " + user.getExperience() + " ponttal";

                    tvLevel.setText(levelMsg);
                    pgsBar.setProgress(user.getExperience());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findItemsOfNode(valueEventListener, usersReference);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseLogin.detachListener();
    }

    private void initProgressBar() {
        pgsBar.setMax(400);
        pgsBar.setProgress(0);
    }

    private void initButtonGoodDay() {
        btnGoodDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            List<Exercise> exercises = new ArrayList<>();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                exercises.add(snapshot.getValue(Exercise.class));
                            }
                            Exercise selectedExercise = ExerciseUtils.getRandomExercise(exercises);
                            Intent intent = new Intent(view.getContext(), TimerActivity.class);
                            intent.putExtra(intentExerciseString, selectedExercise);
                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                firebaseServer.findItemsOfNode(valueEventListener, exercisesReference);
            }
        });

    }





}
