package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.FoodGroupsAdapter;
import com.example.tomizzje.garagecross.enums.FoodGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NutritionActivity extends MenuBaseActivity {

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
        String title = "Táplálkozás";
        tvExerciseListTitle.setText(title);

        List<FoodGroups> foodGroups = new ArrayList<>(Arrays.asList(FoodGroups.values()));
        Log.d("hello", "hello");
        initAdapter(foodGroups);
    }



    private void initAdapter(List<FoodGroups> food) {

        final FoodGroupsAdapter adapter = new FoodGroupsAdapter(food);
        rvExercises.setAdapter(adapter);
        LinearLayoutManager foodLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvExercises.setLayoutManager(foodLayoutManager);
    }


}
