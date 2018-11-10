package com.example.tomizzje.garagecross.utils;

public class UserUtils {

    public static String getLevelByExperience(int exp) {
        if(exp < 100){
            return "könnyű";
        } else if( exp < 200) {
            return "középhaladó";
        } else if( exp < 400) {
            return "haladó";
        } else {
            return "profi";
        }
    }
}
