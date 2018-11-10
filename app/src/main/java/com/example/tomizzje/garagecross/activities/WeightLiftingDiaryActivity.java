package com.example.tomizzje.garagecross.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tomizzje.garagecross.adapters.RecordAdapter;

import com.example.tomizzje.garagecross.models.Record;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeightLiftingDiaryActivity extends MenuBaseActivity implements ValueEventListener {


    @BindView(R.id.rvRecords) RecyclerView rvRecords;

    @BindView(R.id.fab_add) FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weightlifting_diary);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseServer.findAll(this, "records");

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               IntentToInsert();
            }
        });



    }

    private void IntentToInsert() {
        Intent intent = new Intent(this, InsertRecordActivity.class);
        startActivity(intent);
    }

    private void initAdapter(List<Record> records) {
        final RecordAdapter adapter = new RecordAdapter(records);
        rvRecords.setAdapter(adapter);
        LinearLayoutManager recordLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecords.setLayoutManager(recordLayoutManager);
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String currentUser = firebaseLogin.getCurrentUser();

        if(dataSnapshot.exists()) {
            List<Record> records = new ArrayList<>();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if(currentUser.equals(snapshot.getValue(Record.class).getUser_id())) {
                    records.add(snapshot.getValue(Record.class));
                }
            }
            initAdapter(records);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}
