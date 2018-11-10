package com.example.tomizzje.garagecross.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseServer {

    private DatabaseReference databaseReference;

    public FirebaseServer() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }


    public void findAll(ValueEventListener valueEventListener, String reference) {
        databaseReference.child(reference).addListenerForSingleValueEvent(valueEventListener);
   }

    public void findAllOrderBy(ValueEventListener valueEventListener, String reference) {
        Query query = databaseReference.child(reference).orderByChild("popularity");
        query.addValueEventListener(valueEventListener);
    }

    public void findAllUser(ValueEventListener valueEventListener, String reference, String user_id) {
        Query query = databaseReference.child(reference).orderByChild("user_id").equalTo(user_id);
        query.addValueEventListener(valueEventListener);
    }

    public void updateFavoriteExercise(String ref, String id, String value ) {
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

    public void deleteExercise(String ref, String id) {
        databaseReference.child(ref).child(id).removeValue();
    }

   public void insertDoneExercise(IExercise exercise, String ref) {
        databaseReference.child(ref).push().setValue(exercise);
    }

    public void insertExercise(Exercise exercise, String ref) {
        String key = databaseReference.child(ref).push().getKey();
        if(key !=null) {
            exercise.setUid(key);
            databaseReference.child(ref).child(key).setValue(exercise);
        }
    }

    public void insertRecord(Record record, String ref) {
        String key = databaseReference.child(ref).push().getKey();
        if(key !=null) {
            record.setPushId(key);
            databaseReference.child(ref).child(key).setValue(record);
        }
    }

    public void insertUser(User user, String ref) {
        String key = databaseReference.child(ref).push().getKey();
        if(key != null) {
            user.setPushId(key);
            databaseReference.child(ref).child(key).setValue(user);
        }
    }

    public void deleteRecord(Record record, String ref) {
        databaseReference.child(ref).child(record.getPushId()).removeValue();
    }

    public void updateRecord(Record saveRecord, String ref) {
        databaseReference.child(ref).child(saveRecord.getPushId()).setValue(saveRecord);
    }

    public void insertFood(Food food, String ref) {
        String key = databaseReference.child(ref).push().getKey();
        if(key != null) {
            food.setPushId(key);
            databaseReference.child(ref).child(key).setValue(food);
        }
    }

    public void modifyFood(Food food, String ref) {
        databaseReference.child(ref).child(food.getPushId()).setValue(food);
    }

    public void deleteFood(Food food, String ref) {
        databaseReference.child(ref).child(food.getPushId()).removeValue();
    }
}
