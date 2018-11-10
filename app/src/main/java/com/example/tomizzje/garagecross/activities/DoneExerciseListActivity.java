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

import com.example.tomizzje.garagecross.adapters.DoneExerciseAdapter;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.models.DoneExercise;
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

public class DoneExerciseListActivity extends MenuBaseActivity implements ValueEventListener {

    @BindView(R.id.tvExerciseListTitle) TextView tvExerciseListTitle;

    @BindView(R.id.rvExercises) RecyclerView rvExercises;

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
        String title = "Elvégzett feladatsorok";
        tvExerciseListTitle.setText(title);

        firebaseServer.findAll(this, "doneExercises");

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String currentUser = firebaseLogin.getCurrentUser();
        Log.d("HEYHO:", currentUser);
        if(dataSnapshot.exists()) {
            List<DoneExercise> doneExercises = new ArrayList<>();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if(currentUser.equals(snapshot.getValue(DoneExercise.class).getUser_id())) {
                    doneExercises.add(snapshot.getValue(DoneExercise.class));
                }
            }
            initAdapter(doneExercises);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    private void initAdapter(List<DoneExercise> doneExercises) {

        final DoneExerciseAdapter adapter = new DoneExerciseAdapter(doneExercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager doneExercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(doneExercisesLayoutManager);
    }
}
