package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    User user;

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

    private void initButtonStronger() {
        btnStronger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null) {
                    Difficulty difficulty = Difficulty.getStrongerDifficultyLevel(Difficulty.getDifficultyLevelByExperience(user.getExperience()));
                    initExercises(difficulty);
                }
            }
        });
    }

    private void initUser() {
        ValueEventListener UserValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(User.class).getUser_id().equals(firebaseLogin.getCurrentUser())){
                            user = snapshot.getValue(User.class);
                            if(user !=null){
                                String message = "A te szinted jelenleg: " + Difficulty.getDifficultyLevelByExperience(user.getExperience()).toString();
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

            }
        };
        firebaseServer.findItemsOfNode(UserValueEventListener, usersReference);

    }

    private void initAdapter(ArrayList<Exercise> exercises) {
        final ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }



    private void initExercises(final Difficulty difficulty) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    ArrayList<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Exercise.class).getDifficulty().equals(difficulty.name())){
                        exercises.add(snapshot.getValue(Exercise.class));
                        }
                    }

                    tvInfo.setVisibility(View.GONE);
                    if(exercises.isEmpty()){
                        tvInfo.setVisibility(View.VISIBLE);
                    }

                    Collections.reverse(exercises);
                    initAdapter(exercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findExercisesOrderBy(valueEventListener,exercisesReference);
    }
}
