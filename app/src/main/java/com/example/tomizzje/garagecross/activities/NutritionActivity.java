package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.FoodGroupAdapter;
import com.example.tomizzje.garagecross.enums.FoodGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NutritionActivity extends MenuBaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvFood)
    RecyclerView rvFood;

    @BindString(R.string.food_list_title)
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTitle.setText(title);
        List<FoodGroup> foodGroups = new ArrayList<>(Arrays.asList(FoodGroup.values()));
        initAdapter(foodGroups);
    }

    private void initAdapter(List<FoodGroup> food) {
        final FoodGroupAdapter adapter = new FoodGroupAdapter(food);
        rvFood.setAdapter(adapter);
        LinearLayoutManager foodLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFood.setLayoutManager(foodLayoutManager);
    }
}
