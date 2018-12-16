package com.example.tomizzje.garagecross.utils;

import android.annotation.SuppressLint;

import com.example.tomizzje.garagecross.entities.Exercise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Utils {

    /**
     * This method returns the current time
     * @return String
     */
    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return(dateFormat.format(calendar.getTime())); //2018/11/16 12:08:43
    }


    /**
     * This method returns the average of rates
     * @param rates list of rates
     * @return float
     */
    public static float getRate(List<Float> rates ) {
        float rate = 0;
        int numberOfRates = rates.size();
        for(float value : rates) {
            rate+= value;
        }
        return rate / numberOfRates;
    }

    /**
     * This method returns a name in hungarian order
     * @param name name
     * @return String name
     */
    public static String fixDisplayName(String name) {
        StringBuilder sb = new StringBuilder();
        String[] words = name.split(" ");
        List<String> wordsList = Arrays.asList(words);
        if(wordsList.size() == 1){
            return name;
        }
        Collections.reverse(wordsList);
        for(String s : wordsList){
            sb.append(s);
            sb.append(" ");
        }

        return sb.toString();
    }

}
