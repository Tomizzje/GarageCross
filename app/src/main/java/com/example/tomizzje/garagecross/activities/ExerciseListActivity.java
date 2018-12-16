package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.ExerciseAdapter;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseListActivity extends MenuBaseActivity {

    /**
     * Fields connected by the view and strings.xml
     */

    @BindView(R.id.rvItems)
    RecyclerView rvExercises;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.unknown_error_text)
    String errorToast;

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

    /**
     *  Query the Exercises from the Database, and collect them into a list.
     */
    private void initExerciseList() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    ArrayList<Exercise> exercises = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Exercise.class) != null){
                            exercises.add(snapshot.getValue(Exercise.class));
                        }
                    }
                    Collections.reverse(exercises);
                    initAdapter(exercises);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
            }
        };
        firebaseServer.findExercisesOrderBy(valueEventListener, exercisesReference);
    }



    /**
     * Initialize the adapter with the list of exercises
     * @param exercises list
     */
    private void initAdapter(ArrayList<Exercise> exercises) {
        final ExerciseAdapter adapter = new ExerciseAdapter(exercises);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(exercisesLayoutManager);
    }

}
