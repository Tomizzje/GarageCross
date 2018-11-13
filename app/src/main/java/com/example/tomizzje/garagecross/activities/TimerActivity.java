package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.adapters.ImageAdapter;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.models.DoneExercise;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.models.User;
import com.example.tomizzje.garagecross.utils.ExerciseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerActivity extends BaseActivity {

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtDesc) TextView txtDesc;
    @BindView(R.id.btnPlay) ImageButton btnPlay;
    @BindView(R.id.btnReset) ImageButton btnReset;
    @BindView(R.id.btnReady) ImageButton btnReady;
    @BindView(R.id.simpleChronometer) Chronometer simpleChronometer;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.rvImages) RecyclerView rvImages;

    private boolean isPaused = true;

    private Exercise exercise;
    private long lastPause = 0;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Exercise exercise = (Exercise) intent.getSerializableExtra("Exercise");
        if (exercise == null) {
            exercise = new Exercise();
        }
        this.exercise = exercise;
        txtTitle.setText(exercise.getTitle());
        txtDesc.setText(exercise.getDescription());

        if(exercise.getPicturesUrl() != null) {
            ArrayList<String> imagesList = new ArrayList<>(exercise.getPicturesUrl().values());
            initadapter(imagesList);
        }

        /*
                if(exercise.getPicturesUrl() != null) {
            for(String s : exercise.getPicturesUrl().values()) {
                imagesList.add(s);
            }
            initadapter(imagesList);
        }

         */
        initUser();
        initButtonClickListeners();
    }



    private void initadapter(ArrayList<String> imagesList) {
        final ImageAdapter adapter = new ImageAdapter(imagesList);
        rvImages.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(exercisesLayoutManager);
    }

    private void initUser(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() ) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       if(snapshot.getValue(User.class).getUser_id().equals(firebaseLogin.getCurrentUser())){
                           user = snapshot.getValue(User.class);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findAll(valueEventListener,"users");
    }

    private void initButtonClickListeners() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = !isPaused;

                if(!isPaused) {
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    simpleChronometer.setBase(SystemClock.elapsedRealtime() + lastPause);
                    simpleChronometer.start();
                }
                else{
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    lastPause = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                    simpleChronometer.stop();
                }

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                isPaused = true;
                btnPlay.setImageResource(android.R.drawable.ic_media_play);
                simpleChronometer.stop();
                vibrationUtil.vibrate(3000);

            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDoneExercise();
                backToList();
            }
        });
    }

    private void backToList() {
        Intent intent = new Intent(this, ExerciseListActivity.class);
        startActivity(intent);
    }



    private void saveDoneExercise() {
        String currentTime = ExerciseUtils.getCurrentTime();
        String elapsedTime = String.valueOf(simpleChronometer.getText());
        String title = String.valueOf(txtTitle.getText());
        String currentUser = firebaseLogin.getCurrentUser();



        String id = exercise.getPushId();

        // TODO
        if(exercise.getRatedUsers() == null && ratingBar.getRating() != 0 ) {
            firebaseServer.rateExercise("exercises", id, currentUser, ratingBar.getRating());
            firebaseServer.updatePopularity("exercises", id, ratingBar.getRating());
        } else if(ratingBar.getRating() != 0 && !exercise.getRatedUsers().containsKey(currentUser)){
            firebaseServer.rateExercise("exercises", id, currentUser, ratingBar.getRating());
            firebaseServer.updatePopularity("exercises", id, ExerciseUtils.getRate(exercise));
        } else {
            Toast.makeText(this, "You can rate this exercise only once",
                    Toast.LENGTH_LONG).show();
        }

        // TODO

        //doneExercise = new DoneExercise(title, description, elapsedTime, currentTime, currentUser);
        DoneExercise doneExercise = new DoneExercise("default", title, elapsedTime, currentTime, user);
        firebaseServer.insertEntity(doneExercise, "doneExercises");


        int point = Difficulty.getDifficultyPoints(Difficulty.getDifficultyByName(exercise.getDifficulty()));

        int value =user.getExperience() +  point;
        firebaseServer.updateExperience("users", user.getPushId(),value);

    }

}
