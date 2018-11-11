package com.example.tomizzje.garagecross.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class DoneExercise implements Serializable,IBaseEntity {

    @Setter
    @Getter
    private String pushId;

    @Getter
    @Setter
    private String title;

    @Setter
    @Getter
    private String timeElapsed;

    @Setter
    @Getter
    private String dateTime;

    @Setter
    @Getter
    private User user;

}
