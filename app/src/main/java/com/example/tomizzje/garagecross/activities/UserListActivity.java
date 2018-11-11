package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.UserListAdapter;
import com.example.tomizzje.garagecross.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;

public class UserListActivity extends MenuBaseActivity{

    @BindView(R.id.rvItems) RecyclerView rvItems;

    @BindView(R.id.tvListTitle) TextView tvListTitle;

    @BindString(R.string.user_list_title) String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvListTitle.setText(title);
        initUsers();
    }

    private void initUsers() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
        };
        firebaseServer.findAll(valueEventListener, "users");
    }

    private void initAdapter(List<User> users) {
        final UserListAdapter adapter = new UserListAdapter(users);
        rvItems.setAdapter(adapter);
        LinearLayoutManager usersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(usersLayoutManager);
    }
}
