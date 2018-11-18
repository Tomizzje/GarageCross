package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.ShareAdapter;
import com.example.tomizzje.garagecross.entities.Share;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareListActivity extends MenuBaseActivity {
    @BindView(R.id.tvListTitle)
    TextView tvListTitle;

    @BindView(R.id.rvItems)
    RecyclerView rvItems;

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindString(R.string.share_list_title)
    String title;

    @BindString(R.string.share_list_no_data)
    String tvInfoLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvListTitle.setText(title);
        tvInfo.setText(tvInfoLabel);
        initDoneExerciseList();

    }

    private void initDoneExerciseList() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUser = firebaseLogin.getCurrentUser();
                if(dataSnapshot.exists()) {
                    ArrayList<Share> shares = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(currentUser.equals(snapshot.getValue(Share.class).getRecipient())) {
                            shares.add(snapshot.getValue(Share.class));
                        }
                    }
                    tvInfo.setVisibility(View.GONE);
                    if(shares.isEmpty()){
                        tvInfo.setVisibility(View.VISIBLE);
                    }
                    initAdapter(shares);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findAll(valueEventListener, "shares");

    }

    private void initAdapter(ArrayList<Share> shares) {

        final ShareAdapter adapter = new ShareAdapter(shares);
        rvItems.setAdapter(adapter);
        LinearLayoutManager shareLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(shareLayoutManager);
    }
}
