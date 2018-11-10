package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.DoneExerciseAdapter;
import com.example.tomizzje.garagecross.adapters.FoodAdapter;
import com.example.tomizzje.garagecross.enums.FoodGroups;
import com.example.tomizzje.garagecross.models.DoneExercise;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.models.Food;
import com.example.tomizzje.garagecross.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodListActivity extends MenuBaseActivity {
    //TODO
    @BindView(R.id.tvExerciseListTitle)
    TextView tvExerciseListTitle;

    @BindView(R.id.rvExercises)
    RecyclerView rvExercises;

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



        Intent intent = getIntent();
        FoodGroups foodGroups = (FoodGroups) intent.getSerializableExtra("FoodGroup");
        if(foodGroups != null){
            tvExerciseListTitle.setText(foodGroups.getString());
        }


        initFood();


    }

    private void initFood() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<String> food = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        food.add(snapshot.getValue(Food.class).getName());
                    }
                    initAdapter(food);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebaseServer.findAll(valueEventListener, "food");
    }


    private void initAdapter(List<String> food) {

        final FoodAdapter adapter = new FoodAdapter(food);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager doneExercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(doneExercisesLayoutManager);
    }
}
