package com.example.tomizzje.garagecross.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tomizzje.garagecross.adapters.RecordAdapter;

import com.example.tomizzje.garagecross.entities.Record;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeightLiftingDiaryActivity extends MenuBaseActivity{


    @BindView(R.id.rvRecords)
    RecyclerView rvRecords;

    @BindView(R.id.fab_add)
    FloatingActionButton fab_add;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindString(R.string.database_reference_records)
    String recordsReference;

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
        initRecords();
        initFloatingActionButton();
    }

    private void initFloatingActionButton() {
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentToInsert();
            }
        });
    }

    private void initRecords() {
        ValueEventListener valueEventListener = new ValueEventListener() {
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
                    tvInfo.setVisibility(View.GONE);
                    if(records.isEmpty()){
                        tvInfo.setVisibility(View.VISIBLE);
                    }
                    initAdapter(records);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findItemsOfNode(valueEventListener,recordsReference);
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
}
