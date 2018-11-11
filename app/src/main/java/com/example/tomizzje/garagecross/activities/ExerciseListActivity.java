package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.ExerciseAdapter;
import com.example.tomizzje.garagecross.models.Exercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseListActivity extends MenuBaseActivity {

    @BindView(R.id.rvItems) RecyclerView rvExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initExerciseList();

    }

    private void initExerciseList() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    List<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        exercises.add(snapshot.getValue(Exercise.class));

                    }
                    Collections.reverse(exercises);
                    initAdapter(exercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findAllOrderBy(valueEventListener, "exercises");
    }

    private void initAdapter(List<Exercise> exercises) {
        final ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }

}
