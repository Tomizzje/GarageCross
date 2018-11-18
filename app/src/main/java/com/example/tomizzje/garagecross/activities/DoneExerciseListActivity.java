package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tomizzje.garagecross.adapters.DoneExerciseAdapter;
import com.example.tomizzje.garagecross.entities.DoneExercise;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoneExerciseListActivity extends MenuBaseActivity{

    @BindView(R.id.tvListTitle)
    TextView tvExerciseListTitle;

    @BindView(R.id.rvItems)
    RecyclerView rvExercises;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindString(R.string.done_exercise_list_title)
    String title;

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
        tvExerciseListTitle.setText(title);
        initDoneExerciseList();
    }

    private void initDoneExerciseList() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUser = firebaseLogin.getCurrentUser();
                if(dataSnapshot.exists()) {
                    List<DoneExercise> doneExercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(currentUser.equals(snapshot.getValue(DoneExercise.class).getUser().getUser_id())) {
                            doneExercises.add(snapshot.getValue(DoneExercise.class));
                        }
                    }

                    tvInfo.setVisibility(View.GONE);
                    if(doneExercises.isEmpty()){
                        tvInfo.setVisibility(View.VISIBLE);
                    }
                    initAdapter(doneExercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findAll(valueEventListener, "doneExercises");
    }

    private void initAdapter(List<DoneExercise> doneExercises) {

        final DoneExerciseAdapter adapter = new DoneExerciseAdapter(doneExercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager doneExercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(doneExercisesLayoutManager);
    }
}
