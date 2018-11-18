package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.enums.FoodGroup;
import com.example.tomizzje.garagecross.entities.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertFoodActivity extends BaseActivity {

    @BindView(R.id.tvTitle) TextView tvTitle;

    @BindView(R.id.spnFoodGroup)
    Spinner spnFoodGroup;

    @BindView(R.id.txtFood) EditText txtFood;

    @BindView(R.id.btnSave) Button btnSave;

    @BindView(R.id.btnDelete) Button btnDelete;

    private boolean toModify;

    private Food food;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_food);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSpinner();
        initFood();
        initSave();
        initDelete();

    }

    private void initFood() {


        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra("ModifyFood");
        String foodGroupType = intent.getStringExtra("FoodGroup");
        if(foodGroupType != null){
            int position = getSpinTextPosition(foodGroupType);
            spnFoodGroup.setSelection(position);
        }

        if (food == null) {
            food = new Food();
            toModify = false;
            btnDelete.setText("Visszaállít");

        } else {
            txtFood.setText(food.getName());
            txtFood.setSelection(food.getName().length());

            btnDelete.setText("Törlés");

            String text = FoodGroup.getFoodGroupByName(food.getFoodGroups()).toString();
            int position = getSpinTextPosition(text);
            spnFoodGroup.setSelection(position);

            toModify = true;
        }
        this.food = food;
    }

    private void initSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtFood.getText() !=null && txtFood.length() > 0) {
                    food.setName(txtFood.getText().toString());
                    String foodGroup= FoodGroup.getFoodGroupByString(spnFoodGroup.getSelectedItem().toString()).name();
                    if(foodGroup != null){
                        food.setFoodGroups(foodGroup);
                    }
                    //TODO SAVE
                    if(toModify){
                        firebaseServer.modifyFood(food, "food");
                    }else{
                        firebaseServer.insertEntity(food, "food");
                    }
                    backToList();
                }
            }
        });
    }


    private void initSpinner() {
        final String[] foodGroups = FoodGroup.getFoodGroupValuesString();
        AdapterView.OnItemSelectedListener itemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), foodGroups[i], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(getApplicationContext(), difficulties[0], Toast.LENGTH_LONG).show();
            }
        };
        spnFoodGroup.setOnItemSelectedListener(itemClickListener);


        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item, foodGroups);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFoodGroup.setAdapter(aa);
    }

    private int getSpinTextPosition(String text) {
        for(int i=0;i<spnFoodGroup.getAdapter().getCount();++i){
            if(spnFoodGroup.getAdapter().getItem(i).toString().equals(text)){
                return i;
            }
        }
        return 0;
    }


    private void initDelete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toModify){
                    firebaseServer.deleteFood(food, "food");
                }else{
                    txtFood.setText("");
                }
                backToList();
            }
        });
    }

    private void backToList() {
        Intent intent = new Intent(this, NutritionActivity.class);
        startActivity(intent);
    }



}
