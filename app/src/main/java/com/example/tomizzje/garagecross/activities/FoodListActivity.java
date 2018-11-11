package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.FoodAdapter;
import com.example.tomizzje.garagecross.enums.FoodGroup;
import com.example.tomizzje.garagecross.models.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodListActivity extends MenuBaseActivity {
    //TODO
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.rvFood)
    RecyclerView rvFood;

    @BindView(R.id.btnAdd)
    Button btnAdd;


    private FoodGroup foodGroup;

    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnAdd.setVisibility(View.GONE);



        Intent intent = getIntent();
        FoodGroup foodGroup = (FoodGroup) intent.getSerializableExtra("FoodGroup");
        if(foodGroup != null){
            this.foodGroup = foodGroup;
            tvTitle.setText(foodGroup.getUserFriendlyString());
            initAdministrator();


        }

    }


    private void initAddButton() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodListActivity.this, InsertFoodActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initFood() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Food> food = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(foodGroup != null && foodGroup.toString().equals(snapshot.getValue(Food.class).getFoodGroups()))
                        food.add(snapshot.getValue(Food.class));
                    }
                    initAdapter(food, isAdmin);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebaseServer.findAll(valueEventListener, "food");

    }


    private void initAdministrator() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.getKey().equals(firebaseLogin.getCurrentUser())){

                            initAddButton();
                            btnAdd.setVisibility(View.VISIBLE);
                            isAdmin = true;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findAll(valueEventListener,"administrators");
        initFood();
    }




    private void initAdapter(List<Food> food, boolean isAdmin) {
        Log.d("HEYHO3", String.valueOf(isAdmin));
        final FoodAdapter adapter = new FoodAdapter(food, isAdmin);
        rvFood.setAdapter(adapter);
        LinearLayoutManager doneExercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFood.setLayoutManager(doneExercisesLayoutManager);
    }
}
