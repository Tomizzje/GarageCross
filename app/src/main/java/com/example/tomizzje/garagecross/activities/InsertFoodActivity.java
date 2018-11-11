package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.enums.FoodGroup;
import com.example.tomizzje.garagecross.models.Food;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertFoodActivity extends BaseActivity {

    @BindView(R.id.tvTitle) TextView tvTitle;

    @BindView(R.id.numberPicker) NumberPicker numberPicker;

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
        initFood();
        initSave();
        initDelete();

    }

    private void initFood() {

        setNumberPicker();
        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra("ModifyFood");
        if (food == null) {
            food = new Food();
            toModify = false;
            btnDelete.setText("Visszaállít");

        } else {
            txtFood.setText(food.getName());
            txtFood.setSelection(food.getName().length());
            numberPicker.setValue(FoodGroup.getFoodGroupsCode(food.getFoodGroups()));
            btnDelete.setText("Törlés");
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
                    String result = FoodGroup.getFoodGroups(numberPicker.getValue()).toString();
                    if(result != null){
                        food.setFoodGroups(result);
                    }
                    //TODO SAVE
                    if(toModify){
                        firebaseServer.modifyFood(food, "food");
                    }else{
                        firebaseServer.insertEntity(food, "food");
                        Log.d("HEYHO", food.toString());
                    }
                    backToList();
                }
            }
        });
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


    private void setNumberPicker() {
        FoodGroup[] foodGroups = FoodGroup.values();
        String[] groups = new String[foodGroups.length];
        for(int i=0;i<foodGroups.length;++i){
            groups[i] = foodGroups[i].toString();
        }
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(groups);
        numberPicker.setMaxValue(groups.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);
    }
}
