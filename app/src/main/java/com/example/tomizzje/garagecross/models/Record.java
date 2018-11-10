package com.example.tomizzje.garagecross.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Record  implements Serializable {

    @Setter
    @Getter
    private String user_id;

    @Setter
    @Getter
    private String date;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String pushId;





}
