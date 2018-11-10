package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.UserListAdapter;
import com.example.tomizzje.garagecross.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends MenuBaseActivity implements ValueEventListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donelist);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseLogin.detachListener();
    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseServer.findAll(this, "users");
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String currentUser = firebaseLogin.getCurrentUser();
        Log.d("HEYHO:", currentUser);
        if(dataSnapshot.exists()) {
            List<User> users = new ArrayList<>();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    users.add(snapshot.getValue(User.class));
            }
            initAdapter(users);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    private void initAdapter(List<User> users) {
        RecyclerView rvDoneExercises = (RecyclerView) findViewById(R.id.rvDoneExercises);
        final UserListAdapter adapter = new UserListAdapter(users);
        rvDoneExercises.setAdapter(adapter);
        LinearLayoutManager doneExercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDoneExercises.setLayoutManager(doneExercisesLayoutManager);
    }
}
