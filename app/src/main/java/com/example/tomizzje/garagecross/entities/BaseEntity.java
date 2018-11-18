package com.example.tomizzje.garagecross.entities;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class BaseEntity implements IBaseEntity, Serializable {

    @Getter
    @Setter
    private String pushId;

    public BaseEntity(){
        this.pushId = "defaultPushId";
    }
}
