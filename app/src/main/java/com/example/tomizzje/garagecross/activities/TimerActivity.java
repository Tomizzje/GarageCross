package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.adapters.ImageAdapter;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.entities.DoneExercise;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.entities.User;
import com.example.tomizzje.garagecross.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerActivity extends BaseActivity {

    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.txtDesc)
    TextView txtDesc;

    @BindView(R.id.btnPlay)
    ImageButton btnPlay;

    @BindView(R.id.btnReset)
    ImageButton btnReset;

    @BindView(R.id.btnReady)
    ImageButton btnReady;

    @BindView(R.id.simpleChronometer)
    Chronometer simpleChronometer;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.rvImages)
    RecyclerView rvImages;

    @BindView(R.id.tvPictureInfo)
    TextView tvPictureInfo;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.database_reference_doneExercises)
    String doneExercisesReference;

    @BindString(R.string.database_reference_users)
    String usersReference;

    @BindString(R.string.intent_bundle_key_select_exercise)
    String intentExerciseText;

    @BindString(R.string.timer_rate_exercise_toast)
    String rateExerciseToast;

    @BindString(R.string.timer_finished_with_rating)
    String RatedFinishedExerciseToast;

    @BindString(R.string.timer_finished_without_rating)
    String FinishedExerciseToast;

    private boolean isPaused = true;

    private Exercise exercise;
    private long lastPause = 0;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Exercise exercise = (Exercise) intent.getSerializableExtra(intentExerciseText);
        if (exercise == null) {
            exercise = new Exercise();
        }
        this.exercise = exercise;
        txtTitle.setText(exercise.getTitle());
        txtDesc.setText(exercise.getDescription());

        if(exercise.getPicturesUrl() != null) {
            ArrayList<String> imagesList = new ArrayList<>(exercise.getPicturesUrl().values());
            initadapter(imagesList);
        }else {
            tvPictureInfo.setVisibility(View.VISIBLE);
        }

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
        firebaseServer.findItemsOfNode(valueEventListener,usersReference);
    }

    private void initButtonClickListeners() {
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPaused = !isPaused;

                if(!isPaused) {
                    //pause
                    btnPlay.setImageResource(R.drawable.ic_baseline_pause_48px);
                    simpleChronometer.setBase(SystemClock.elapsedRealtime() + lastPause);
                    simpleChronometer.start();
                }
                else{
                    btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_48px);
                    lastPause = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                    simpleChronometer.stop();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                //vibrationUtil.vibrate(3000);

            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDoneExercise();
                resetTimer();
                backToList();
            }
        });
    }

    private void backToList() {
        Intent intent = new Intent(this, DoneExerciseListActivity.class);
        startActivity(intent);
    }



    private void saveDoneExercise() {
        String currentTime = Utils.getCurrentTime();
        String elapsedTime = String.valueOf(simpleChronometer.getText());
        String title = String.valueOf(txtTitle.getText());
        String currentUser = firebaseLogin.getCurrentUser();
        String id = exercise.getPushId();

        checkRatingBar(currentUser, id);

        saveToDoneExercises(title, elapsedTime, currentTime);

        updateUserExperience();
    }

    private void updateUserExperience() {
        int point = Difficulty.getDifficultyPoints(Difficulty.getDifficultyByName(exercise.getDifficulty()));
        int value =user.getExperience() +  point;
        firebaseServer.updateExperience(usersReference, user.getPushId(),value);
    }

    private void saveToDoneExercises(String title, String elapsedTime, String currentTime) {
        DoneExercise doneExercise = new DoneExercise(title, elapsedTime, currentTime, user);
        firebaseServer.insertEntity(doneExercise, doneExercisesReference);
    }

    private void checkRatingBar(String currentUser, String id) {
        if((exercise.getRatedUsers() == null || !exercise.getRatedUsers().containsKey(currentUser)) && ratingBar.getRating() !=0){
            firebaseServer.rateExercise(exercisesReference, id, currentUser, ratingBar.getRating());
            firebaseServer.updatePopularity(exercisesReference, id, ratingBar.getRating());
            Toast.makeText(this, RatedFinishedExerciseToast,
                    Toast.LENGTH_LONG).show();
        }else if(ratingBar.getRating() == 0){
            Toast.makeText(this, FinishedExerciseToast,
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, rateExerciseToast,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void resetTimer(){
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        lastPause = 0;
        isPaused = true;
        btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_48px);
        simpleChronometer.stop();
    }
}
