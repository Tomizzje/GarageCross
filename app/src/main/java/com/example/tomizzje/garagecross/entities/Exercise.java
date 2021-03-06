package com.example.tomizzje.garagecross.entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Exercise extends BaseEntity implements Serializable {

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String creatorUser_id;

    @Getter
    @Setter
    private String difficulty;

    @Getter
    @Setter
    private float popularity;

    @Getter
    @Setter
    private Map<String,String> favoritedUsers;

    @Getter
    @Setter
    private Map<String,Float> ratedUsers;

    @Getter
    @Setter
    private Map<String,String> picturesUrl;

    public Exercise(String title, String description) {
        super();
        this.title = title;
        this.description = description;
        this.creatorUser_id = "";
        this.difficulty = "";
        this.popularity = 0;
        this.favoritedUsers = new HashMap<>();
        this.ratedUsers = new HashMap<>();
    }

    public Exercise(String title, String description, String usersId, String difficulty, HashMap<String,String> picturesUrl) {
        super();
        this.title = title;
        this.description = description;
        this.creatorUser_id = usersId;
        this.difficulty = difficulty;
        this.popularity = 0;
        this.favoritedUsers = new HashMap<>();
        this.ratedUsers = new HashMap<>();
        this.picturesUrl = picturesUrl;
    }
}
