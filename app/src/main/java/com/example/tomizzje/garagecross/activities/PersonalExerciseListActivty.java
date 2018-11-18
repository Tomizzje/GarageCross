package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tomizzje.garagecross.adapters.PersonalExerciseAdapter;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalExerciseListActivty extends MenuBaseActivity{
    @BindView(R.id.rvItems)
    RecyclerView rvExercises;

    @BindView(R.id.tvListTitle)
    TextView tvExerciseListTitle;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindView(R.id.fabAddExercise)
    FloatingActionButton fabAddExercise;

    @BindString(R.string.personal_exercise_list_title)
    String title;

    @BindString(R.string.personal_exercise_no_data)
    String tvInfoLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_exercise_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvExerciseListTitle.setText(title);
        tvInfo.setText(tvInfoLabel);
        initPersonalExercises();
        initFloatinActionButton();
    }

    private void initFloatinActionButton() {
        fabAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InsertExerciseActivity.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    private void initPersonalExercises() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    List<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(firebaseLogin.getCurrentUser().equals(snapshot.getValue(Exercise.class).getCreatorUser_id())){
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
        firebaseServer.findAllOrderBy(valueEventListener, "exercises");
    }

    private void initAdapter(List<Exercise> exercises) {

        final PersonalExerciseAdapter adapter = new PersonalExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }
}

