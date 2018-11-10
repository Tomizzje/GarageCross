package com.example.tomizzje.garagecross.enums;

import android.util.Log;

public enum FoodGroups {
    HÚSOK_VEGETÁRIÁNUSOK(0){
        @Override
        public String getUserFriendlyString(){
            return "Húsok és vegetáriánusok";
        }
    }, LEVÉLZÖLDSÉGEK(1){
        @Override
        public String getUserFriendlyString(){
            return "Levélzöldségek";
        }
    }, KEMÉNYÍTŐ_TARTALMÚ_ZÖLDSÉGEK(2){
        @Override
        public String getUserFriendlyString(){
            return "Keményítő tartalmú zöldségek";
        }
    },GABONAFÉLÉK(3){
        @Override
        public String getUserFriendlyString(){
            return "Gabonafélék";
        }
    },GYÜMÖLCSÖK(4){
        @Override
        public String getUserFriendlyString(){
            return "Gyümölcsök";
        }
    },OLAJOK(5){
        @Override
        public String getUserFriendlyString(){
            return "Olajok";
        }
    },TEJTERMÉKEK(6){
        @Override
        public String getUserFriendlyString(){
            return "Tejtermékek";
        }
    },MAGVAK_ÉS_DIÓFÉLÉK(7){
        @Override
        public String getUserFriendlyString(){
            return "Magvak és diófélék";
        }
    },HÜVELYESEK(8){
        @Override
        public String getUserFriendlyString(){
            return "Hüvelyesek";
        }
    };

    private final int code;

    private FoodGroups(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static FoodGroups getFoodGroups(int code){
        for(FoodGroups f : FoodGroups.values()){
            if(f.code == code){
                return f;
            }
        }
        return null;
    }

    public static int getFoodGroupsCode(String name){
        FoodGroups[] temp = FoodGroups.values();
        for(int i=0;i<temp.length;++i){
            if(temp[i].toString().equals(name)){
                Log.d("HEYHO44", temp[i].toString());
                Log.d("HEYHO44", name);
                return i;
            }
        }
        return -1;
    }







    public abstract String getUserFriendlyString();
}
