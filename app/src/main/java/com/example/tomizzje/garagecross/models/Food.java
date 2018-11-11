package com.example.tomizzje.garagecross.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Food implements Serializable, IBaseEntity {

    @Getter
    @Setter
    private String pushId;

    @Getter
    @Setter
    private String foodGroups;

    @Getter
    @Setter
    private String name;
}
