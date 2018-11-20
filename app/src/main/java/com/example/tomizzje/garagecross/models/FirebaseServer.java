package com.example.tomizzje.garagecross.models;

import com.example.tomizzje.garagecross.entities.BaseEntity;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.entities.Food;
import com.example.tomizzje.garagecross.entities.Record;
import com.example.tomizzje.garagecross.entities.Share;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import lombok.Getter;
import lombok.Setter;

public class FirebaseServer {

    private DatabaseReference databaseReference;

    public FirebaseServer() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public void findItemsOfNode(ValueEventListener valueEventListener, String reference) {
        databaseReference.child(reference).addValueEventListener(valueEventListener);
   }

    public void findExercisesOrderBy(ValueEventListener valueEventListener, String reference) {
        Query query = databaseReference.child(reference).orderByChild("popularity");
        query.addValueEventListener(valueEventListener);
    }

    public void updateFavoritesOfExercise(String ref, String id, String value ) {
      databaseReference.child(ref).child(id).child("favoritedUsers").child(value).setValue(value);
    }

    public void updateExercise(String ref, String id, Exercise exercise) {
        databaseReference.child(ref).child(id).setValue(exercise);
    }

    public void rateExercise(String ref, String id, String value, float ratingBarValue ) {
        databaseReference.child(ref).child(id).child("ratedUsers").child(value).setValue(ratingBarValue);
    }

    public void updatePopularity(String ref, String id, float value) {
        databaseReference.child(ref).child(id).child("popularity").setValue(value);
    }

    public void updateExperience(String ref, String id, int value) {
        databaseReference.child(ref).child(id).child("experience").setValue(value);
    }

    public void deleteFavoriteFromExercise(String ref, String id, String value ) {
        databaseReference.child(ref).child(id).child("favoritedUsers").child(value).removeValue();
    }



    /*public void deleteRecord(Record record, String ref) {
        databaseReference.child(ref).child(record.getPushId()).removeValue();
    }*/

    /*public void updateRecord(Record saveRecord, String ref) {
        databaseReference.child(ref).child(saveRecord.getPushId()).setValue(saveRecord);
    }*/



    /*public void modifyFood(Food food, String ref) {
        databaseReference.child(ref).child(food.getPushId()).setValue(food);
    }*/

    /*public void deleteFood(Food food, String ref) {
        databaseReference.child(ref).child(food.getPushId()).removeValue();
    }*/



    /*public void deleteShare(Share share, String ref){
        databaseReference.child(ref).child(share.getPushId()).removeValue();
    }*/

    public void insertEntity(BaseEntity entity, String ref){
        String key = databaseReference.child(ref).push().getKey();
        if(key != null) {
            entity.setPushId(key);
            databaseReference.child(ref).child(key).setValue(entity);
        }
    }

    public void deleteEntity(BaseEntity entity, String ref){
        databaseReference.child(ref).child(entity.getPushId()).removeValue();
    }

    public void modifyEntity(BaseEntity entity, String ref){
        databaseReference.child(ref).child(entity.getPushId()).setValue(entity);
    }
}
