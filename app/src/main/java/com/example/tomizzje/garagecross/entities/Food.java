package com.example.tomizzje.garagecross.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Food extends BaseEntity implements Serializable {

    @Getter
    @Setter
    private String foodGroups;

    @Getter
    @Setter
    private String name;
}
