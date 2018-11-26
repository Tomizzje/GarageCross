package com.example.tomizzje.garagecross.entities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Share extends BaseEntity implements IBaseEntity {

    @Setter
    @Getter
    private DoneExercise doneExercise;

    @Setter
    @Getter
    private String recipient;

    @Getter
    @Setter
    private String comment;

    @Getter
    @Setter
    private String dateTime;

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.getPushId());
        sb.append(",");
        sb.append(recipient);
        sb.append(",");
        sb.append(comment);
        return sb.toString();
    }
}
