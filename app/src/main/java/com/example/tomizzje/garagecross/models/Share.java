package com.example.tomizzje.garagecross.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Share implements Serializable, IBaseEntity {

    @Getter
    @Setter
    private String pushId;

    @Setter
    @Getter
    private DoneExercise doneExercise;

    @Setter
    @Getter
    private String recipient;

    @Getter
    @Setter
    private String comment;
}
