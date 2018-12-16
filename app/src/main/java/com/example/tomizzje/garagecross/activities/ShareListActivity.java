package com.example.tomizzje.garagecross.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    /**
     * Fields connected by the view and strings.xml
     */

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

    @BindString(R.string.database_reference_shares)
    String sharesReference;

    @BindString(R.string.unknown_error_text)
    String errorToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        tvListTitle.setText(title);
        tvInfo.setText(tvInfoLabel);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initShareList();

    }


    /**
     * Query the user's shares from the database
     */
    private void initShareList() {
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
                    checkListForTextView(shares);
                    initAdapter(shares);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
            }
        };
        firebaseServer.findItemsOfNode(valueEventListener, sharesReference);

    }

    /**
     * Set visibility for Textview depends on list size
     * @param shares list
     */

    private void checkListForTextView(ArrayList<Share> shares) {
        tvInfo.setVisibility(View.GONE);
        if(shares.isEmpty()){
            tvInfo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Initialize the adapter with the list of share
     * @param shares list
     */

    private void initAdapter(ArrayList<Share> shares) {

        final ShareAdapter adapter = new ShareAdapter(shares);
        rvItems.setAdapter(adapter);
        LinearLayoutManager shareLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(shareLayoutManager);
    }
}
