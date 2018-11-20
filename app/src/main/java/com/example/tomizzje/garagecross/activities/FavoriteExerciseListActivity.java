package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tomizzje.garagecross.adapters.ExerciseAdapter;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteExerciseListActivity extends MenuBaseActivity {

    @BindView(R.id.rvItems) RecyclerView rvExercises;

    @BindView(R.id.tvListTitle) TextView tvExerciseListTitle;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindString(R.string.favorite_exercise_list_title)
    String title;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        tvExerciseListTitle.setText(title);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFavoriteExercises();
    }

    private void initFavoriteExercises() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ArrayList<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Exercise.class).getFavoritedUsers() != null && snapshot.getValue(Exercise.class).getFavoritedUsers().containsKey(firebaseLogin.getCurrentUser())){
                            exercises.add(snapshot.getValue(Exercise.class));
                        }
                    }
                    tvInfo.setVisibility(View.GONE);
                    if(exercises.isEmpty()){
                        tvInfo.setVisibility(View.VISIBLE);
                    }
                    initAdapter(exercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findExercisesOrderBy(valueEventListener, exercisesReference);
    }

    private void initAdapter(ArrayList<Exercise> exercises) {

        final ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }
}

