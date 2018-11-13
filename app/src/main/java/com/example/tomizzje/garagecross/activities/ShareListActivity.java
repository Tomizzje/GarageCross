package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.DoneExerciseAdapter;
import com.example.tomizzje.garagecross.adapters.ShareAdapter;
import com.example.tomizzje.garagecross.events.MessageEvent;
import com.example.tomizzje.garagecross.models.DoneExercise;
import com.example.tomizzje.garagecross.models.Share;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareListActivity extends MenuBaseActivity {
    @BindView(R.id.tvListTitle)
    TextView tvListTitle;

    @BindView(R.id.rvItems)
    RecyclerView rvItems;

    @BindString(R.string.share_list_title) String title;

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
        initDoneExerciseList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }

/*    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        //TODO
        Log.d("EVENTBUS2", "heyho");
        //initDoneExerciseList();
    }*/

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
