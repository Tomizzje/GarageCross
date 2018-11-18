package com.example.tomizzje.garagecross.enums;

import android.util.Log;

public enum FoodGroup {
    MEATS_AND_VEGETARIANS("Húsok és vegetáriánusok"),
    GREEN_VEGGIES("Levélzöldségek"),
    STARCHY_VEGGIES("Keményítő tartalmú zöldségek"),
    GRAINS("Gabonafélék"),
    FRUITS("Gyümölcsök"),
    OILS("Olajok"),
    DAIRY("Tejtermékek"),
    NUTS_SEEDS("Magvak és diófélék"),
    LEGUMES("Hüvelyesek");

    private final String userFriendlyString;

    FoodGroup(String userFriendlyString) {
        this.userFriendlyString = userFriendlyString;
    }

    @Override
    public String toString(){
        return userFriendlyString;
    }


    public static FoodGroup getFoodGroupByString(String userFriendlyString){
        for(FoodGroup f : FoodGroup.values()){
            if(f.userFriendlyString.equals(userFriendlyString)){
                return f;
            }
        }
        return MEATS_AND_VEGETARIANS;
    }

    public static FoodGroup getFoodGroupByName(String name){
        for(FoodGroup f : FoodGroup.values()){
            if(f.name().equals(name)){
                return f;
            }
        }
        return MEATS_AND_VEGETARIANS;
    }

    public static String[] getFoodGroupValuesString(){
        FoodGroup[] temp = FoodGroup.values();
        String[] result = new String[temp.length];
        for(int i=0;i<temp.length;++i){
            result[i] = temp[i].toString();
        }
        return result;
    }
}
