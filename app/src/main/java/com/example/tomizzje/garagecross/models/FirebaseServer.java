package com.example.tomizzje.garagecross.models;

import com.example.tomizzje.garagecross.entities.BaseEntity;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseServer {

    private DatabaseReference databaseReference;

    public FirebaseServer() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    /**
     *
     insert , delete , modify, select  general methods for database
     */

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

    public void findItemsOfNode(ValueEventListener valueEventListener, String reference) {
        databaseReference.child(reference).addValueEventListener(valueEventListener);
   }

   /**
        specific object database methods
    */

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


}
