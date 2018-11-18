package com.example.tomizzje.garagecross.entities;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {


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

    public User(String user_id, String email, String name){
        super();
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.experience = 0;
    }

}
