package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.ExerciseAdapter;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.entities.User;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecommendedExerciseListActivity extends MenuBaseActivity  {

    /**
     * Fields connected by the view and strings.xml
     */
    @BindView(R.id.rvItems)
    RecyclerView rvExercises;

    @BindView(R.id.tvListTitle)
    TextView tvExerciseListTitle;

    @BindView(R.id.tvLevel)
    TextView tvLevel;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindView(R.id.btnStronger)
    Button btnStronger;

    @BindString(R.string.database_reference_users)
    String usersReference;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.recommended_exercise_max_level_toast)
    String maximumLevelToast;

    @BindString(R.string.recommended_exercise_next_level_toast)
    String strongerToast;

    @BindString(R.string.recommended_exercise_text)
    String levelText;

    @BindString(R.string.unknown_error_text)
    String errorToast;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_exercise_list);
        ButterKnife.bind(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUser();
        initButtonStronger();
    }

    /**
     * Initialize the button onClickListener
     */
    private void initButtonStronger() {
        btnStronger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null) {
                    Difficulty difficulty = Difficulty.getDifficultyLevelByExperience(user.getExperience());

                    if(difficulty.name().equals(Difficulty.values()[Difficulty.values().length-1].name())){
                        Toast.makeText(view.getContext(), maximumLevelToast,
                                Toast.LENGTH_LONG).show();
                    }else{
                        Difficulty strongerDifficulty = Difficulty.getStrongerDifficultyLevel(Difficulty.getDifficultyLevelByExperience(user.getExperience()));
                        initExercises(strongerDifficulty);
                        Toast.makeText(view.getContext(), strongerToast,
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    /**
     * Query the current user to get the experience from the database
     */
    private void initUser() {
        ValueEventListener UserValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    String currentUser = firebaseLogin.getCurrentUser();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(User.class).getUser_id() != null &&snapshot.getValue(User.class).getUser_id().equals(currentUser)){
                            user = snapshot.getValue(User.class);
                            if(user !=null){
                                String message = levelText + Difficulty.getDifficultyLevelByExperience(user.getExperience()).toString();
                                Difficulty difficulty = Difficulty.getDifficultyLevelByExperience(user.getExperience());
                                tvLevel.setText(message);
                                initExercises(difficulty);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
            }
        };
        firebaseServer.findItemsOfNode(UserValueEventListener, usersReference);

    }

    /**
     * Query the exercises from the database depends on the difficulty
     * @param difficulty depends on the user's experience
     */
    private void initExercises(final Difficulty difficulty) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    ArrayList<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Exercise.class).getDifficulty() != null && snapshot.getValue(Exercise.class).getDifficulty().equals(difficulty.name())){
                        exercises.add(snapshot.getValue(Exercise.class));
                        }
                    }

                    checkListForTextView(exercises);

                    Collections.reverse(exercises);
                    initAdapter(exercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
            }
        };
        firebaseServer.findExercisesOrderBy(valueEventListener,exercisesReference);
    }

    /**
     * Initialize the adapter with the list of exercise
     * @param exercises list
     */
    private void initAdapter(ArrayList<Exercise> exercises) {
        final ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }

    /**
     * Set visibility for Textview depends on list size
     * @param exercises list
     */
    private void checkListForTextView(ArrayList<Exercise> exercises) {
        tvInfo.setVisibility(View.GONE);
        if(exercises.isEmpty()){
            tvInfo.setVisibility(View.VISIBLE);
        }
    }
}
