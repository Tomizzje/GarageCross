package com.example.tomizzje.garagecross.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class DoneExercise extends Exercise implements IExercise {

    @Setter
    @Getter
    String timeElapsed;

    @Setter
    @Getter
    String dateTime;

    @Setter
    @Getter
    String user_id;

    public DoneExercise(String title, String description, String timeElapsed, String dateTime, String user_id) {
        super(title, description);
        this.timeElapsed = timeElapsed;
        this.dateTime = dateTime;
        this.user_id = user_id;
    }
}
