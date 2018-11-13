package com.example.tomizzje.garagecross.events;

import com.example.tomizzje.garagecross.models.User;

public class MessageEvent {

    public final User user;

    public MessageEvent(User user) {
        this.user = user;
    }

    //todo
    public MessageEvent(){
        this.user = null;
    }
}