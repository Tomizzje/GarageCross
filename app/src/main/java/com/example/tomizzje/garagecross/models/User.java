package com.example.tomizzje.garagecross.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Setter
    @Getter
    private String user_id;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String name;

    @Getter
    @Setter
    private int experience;

    @Getter
    @Setter
    private String pushId;

}
