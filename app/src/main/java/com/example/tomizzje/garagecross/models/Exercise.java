package com.example.tomizzje.garagecross.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Exercise implements Serializable, IBaseEntity {

    @Getter
    @Setter
    private String pushId;

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
        this.pushId = "1";
        this.title = title;
        this.description = description;
        this.creatorUser_id = "1";
        this.difficulty = "easy";
        this.popularity = 0;

        this.favoritedUsers = new HashMap<>();
        this.ratedUsers = new HashMap<>();
    }

    public Exercise(String title, String description, String usersId, String difficulty, HashMap<String,String> picturesUrl) {
        this.pushId = "1";
        this.title = title;
        this.description = description;
        this.creatorUser_id = usersId;
        this.difficulty = difficulty;
        this.popularity = 0;

        this.favoritedUsers = new HashMap<>();
        //favoritedUsers.put(usersId, usersId);

        this.ratedUsers = new HashMap<>();
        this.picturesUrl = picturesUrl;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.title);
        sb.append(" ");
        sb.append(this.description);
        sb.append(" ");
        sb.append(this.creatorUser_id);
        sb.append(" ");
        sb.append(this.difficulty);
        sb.append(" ");
        sb.append(this.popularity);
        sb.append(" ");
        return sb.toString();
    }
}
