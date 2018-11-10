package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tomizzje.garagecross.adapters.PersonalExerciseAdapter;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.models.FirebaseLogin;
import com.example.tomizzje.garagecross.models.FirebaseServer;
import com.example.tomizzje.garagecross.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalExerciseListActivty extends MenuBaseActivity implements ValueEventListener {
    @BindView(R.id.rvExercises) RecyclerView rvExercises;

    @BindView(R.id.tvExerciseListTitle) TextView tvExerciseListTitle;

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
        String title = "Saját feladatsorok";
        tvExerciseListTitle.setText(title);
        firebaseServer.findAllOrderBy(this, "exercises");
    }

    private void initAdapter(List<Exercise> exercises) {

        final PersonalExerciseAdapter adapter = new PersonalExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {

            List<Exercise> exercises = new ArrayList<>();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if(firebaseLogin.getCurrentUser().equals(snapshot.getValue(Exercise.class).getCreatorUser_id())){
                    exercises.add(snapshot.getValue(Exercise.class));
                }
            }

            for(Exercise e: exercises) {
                Log.d("Exercises:", e.toString());
            }
            initAdapter(exercises);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}

