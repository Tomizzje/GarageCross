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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertFoodActivity extends BaseActivity {


    /**
     * Fields connected by the view and strings.xml
     */

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.spnFoodGroup)
    Spinner spnFoodGroup;

    @BindView(R.id.txtFood)
    EditText txtFood;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    @BindString(R.string.btnReset)
    String btnResetText;

    @BindString(R.string.btnDelete)
    String btnDeleteText;

    @BindString(R.string.intent_bundle_key_modify_food)
    String intentModifyFood;

    @BindString(R.string.intent_bundle_key_select_foodGroup)
    String intentFoodGroupString;

    @BindString(R.string.database_reference_food)
    String foodReference;

    @BindString(R.string.insert_food_no_food)
    String noFoodToast;

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

    /**
     * Initialize food whether the administrator create a new one or modify
     */
    private void initFood() {
        Intent intent = getIntent();
        Food food = (Food) intent.getSerializableExtra(intentModifyFood);
        String foodGroupType = intent.getStringExtra(intentFoodGroupString);
        if(foodGroupType != null){
            int position = getSpinTextPosition(foodGroupType);
            spnFoodGroup.setSelection(position);
        }

        if (food == null) {
            food = new Food();
            toModify = false;
            btnDelete.setText(btnResetText);

        } else {
            txtFood.setText(food.getName());
            txtFood.setSelection(food.getName().length()); // set focus for Edittext

            btnDelete.setText(btnDeleteText);

            String text = FoodGroup.getFoodGroupByName(food.getFoodGroups()).toString();
            int position = getSpinTextPosition(text);
            spnFoodGroup.setSelection(position);

            toModify = true;
        }
        this.food = food;
    }

    /**
     * This method checks the input and saves/modifies to the database
     */
    private void initSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtFood.getText() !=null && txtFood.length() > 0) {
                    food.setName(txtFood.getText().toString());
                    String foodGroup= FoodGroup.getFoodGroupByString(spnFoodGroup.getSelectedItem().toString()).name();
                    if(foodGroup != null){
                        food.setFoodGroups(foodGroup);
                    }else {
                        Toast.makeText(view.getContext(), noFoodToast,
                                Toast.LENGTH_LONG).show();
                    }

                    if(toModify){
                        firebaseServer.modifyEntity(food, foodReference);
                    }else{
                        firebaseServer.insertEntity(food, foodReference);
                    }
                    backToList();
                }
            }
        });
    }

    /**
     * Initialize the spinner
     */
    private void initSpinner() {
        final String[] foodGroups = FoodGroup.getFoodGroupValuesString();
        AdapterView.OnItemSelectedListener itemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spnFoodGroup.setOnItemSelectedListener(itemClickListener);

        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item, foodGroups);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFoodGroup.setAdapter(aa);
    }

    /**
     * Returns the position of the selected spinner item
     * @param text spinner item
     * @return position
     */
    private int getSpinTextPosition(String text) {
        for(int i=0;i<spnFoodGroup.getAdapter().getCount();++i){
            if(spnFoodGroup.getAdapter().getItem(i).toString().equals(text)){
                return i;
            }
        }
        return 0;
    }

    /**
     * Delete the food from the database
     */
    private void initDelete(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toModify){
                    firebaseServer.deleteEntity(food, foodReference);
                    backToList();
                }else{
                    txtFood.setText("");
                }

            }
        });
    }

    /**
     * Navigate back to the list site
     */
    private void backToList() {
        Intent intent = new Intent(this, NutritionActivity.class);
        startActivity(intent);
    }
}
