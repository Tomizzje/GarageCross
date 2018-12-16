package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.FoodGroupAdapter;
import com.example.tomizzje.garagecross.enums.FoodGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NutritionActivity extends MenuBaseActivity {

    /**
     * Fields connected by the view and strings.xml
     */
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvFood)
    RecyclerView rvFood;

    @BindView(R.id.btnAdd)
    Button btnAdd;

    @BindString(R.string.food_list_title)
    String title;

    @BindString(R.string.database_reference_administrators)
    String administratorsReference;

    @BindString(R.string.intent_bundle_key_select_foodGroup)
    String intentFoodGroupString;


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
        initAdministrator();
    }

    /**
     * Initialize the adapter with the list of FoodGroup
     * @param food list of FoodGroup
     */
    private void initAdapter(List<FoodGroup> food) {
        final FoodGroupAdapter adapter = new FoodGroupAdapter(food);
        rvFood.setAdapter(adapter);
        LinearLayoutManager foodLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFood.setLayoutManager(foodLayoutManager);
    }

    /**
     * Query the administrators from database to check if the user is an administrator
     */
    private void initAdministrator() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.getKey() != null && snapshot.getKey().equals(firebaseLogin.getCurrentUser())){
                            btnAdd.setVisibility(View.VISIBLE);
                            initAddButton();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findItemsOfNode(valueEventListener,administratorsReference);
    }

    /**
     * Initialize add button for administrators
     */
    private void initAddButton() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodGroupType = tvTitle.getText().toString();
                Intent intent = new Intent(NutritionActivity.this, InsertFoodActivity.class);
                intent.putExtra(intentFoodGroupString, foodGroupType);
                startActivity(intent);
            }
        });
    }


}
