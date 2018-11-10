package com.example.tomizzje.garagecross.models;

import com.example.tomizzje.garagecross.enums.FoodGroups;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Food implements Serializable {

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
