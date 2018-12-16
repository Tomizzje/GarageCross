package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    @BindView(R.id.rvItems)
    RecyclerView rvExercises;

    @BindView(R.id.tvListTitle)
    TextView tvExerciseListTitle;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindString(R.string.favorite_exercise_list_title)
    String title;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.favorite_exercise_no_data)
    String tvInfoLabel;

    @BindString(R.string.unknown_error_text)
    String errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        tvExerciseListTitle.setText(title);
        tvInfo.setText(tvInfoLabel);
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

    /**
     *  Query the the user's favorite exercises from the Database, and collect them into a list.
     */
    private void initFavoriteExercises() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    ArrayList<Exercise> exercises = new ArrayList<>();
                    String currentUser = firebaseLogin.getCurrentUser();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(snapshot.getValue(Exercise.class).getFavoritedUsers() != null && snapshot.getValue(Exercise.class).getFavoritedUsers().containsKey(currentUser)){
                            exercises.add(snapshot.getValue(Exercise.class));
                        }
                    }
                    checkListForTextView(exercises);
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

