package com.example.tomizzje.garagecross.utils;

import android.content.Context;
import android.os.Vibrator;

public class VibrationUtil {

    private Vibrator vibrator;

    public VibrationUtil(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate(int length) {
        if(vibrator != null) {
            vibrator.vibrate(length);
        }
    }
}
