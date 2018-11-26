package com.example.tomizzje.garagecross.utils;

import android.annotation.SuppressLint;

import com.example.tomizzje.garagecross.entities.Exercise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class Utils {

    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        return(dateFormat.format(calendar.getTime())); //2018/11/16 12:08:43
    }

    public static float getRate(Exercise exercise) {
        float rate = 0;
        int numberOfRates = exercise.getRatedUsers().size();
        for(float value : exercise.getRatedUsers().values()) {
            rate+= value;
        }
        return rate / numberOfRates;
    }

    public static Exercise getRandomExercise(List<Exercise> exercises ) {
        Random random = new Random();
        return exercises.get(random.nextInt(exercises.size()));
    }

}
