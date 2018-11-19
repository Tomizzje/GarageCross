package com.example.tomizzje.garagecross.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.UserListAdapter;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.events.MessageEvent;
import com.example.tomizzje.garagecross.entities.DoneExercise;
import com.example.tomizzje.garagecross.entities.Share;
import com.example.tomizzje.garagecross.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertShareActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvDialog)
    TextView tvDialog;

    @BindView(R.id.etComment)
    EditText etComment;

    @BindView(R.id.btnShare)
    Button btnShare;

    @BindView(R.id.tvDoneExercise)
    TextView tvDoneExercise;

    private User user;

    private DoneExercise doneExercise;

    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        BaseApplication.getInstance().getBaseComponent().inject(this);
        ButterKnife.bind(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        DoneExercise doneExercise = (DoneExercise) intent.getSerializableExtra("DoneExercise");
        if (doneExercise == null) {
            doneExercise = new DoneExercise();
        }
        this.doneExercise = doneExercise;
        String msgDoneExercise = doneExercise.getTitle() + " " + doneExercise.getTimeElapsed();
        tvDoneExercise.setText(msgDoneExercise);
        initDialog();

        initShareButton();
    }

    private void initShareButton() {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    Toast.makeText(view.getContext(), "Válassz egy felhasználót !",
                            Toast.LENGTH_LONG).show();
                }else {
                    String comment = etComment.getText().toString();
                    Share share = new Share(doneExercise, user.getUser_id(), comment);
                    firebaseServer.insertEntity(share, "shares");

                    Toast.makeText(view.getContext(), "Megosztva !",
                            Toast.LENGTH_LONG).show();
                    backToList();
                }
            }
        });
    }

    private void backToList() {
        Intent intent = new Intent(this, DoneExerciseListActivity.class);
        startActivity(intent);
    }


    private void initDialog() {
        tvDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               initUsers();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        user = event.user;
        tvDialog.setText(user.getName());
        dialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initUsers(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<User> list = new ArrayList<>();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(!(snapshot.getValue(User.class).getUser_id().equals(firebaseLogin.getCurrentUser()))){
                            list.add(snapshot.getValue(User.class));
                        }
                    }
                    initAdapter(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        firebaseServer.findAll(valueEventListener, "users");
    }

    private void initAdapter(List<User> list) {
        dialog = new Dialog(InsertShareActivity.this);
        dialog.setContentView(R.layout.dialog_user);
        dialog.setTitle("Válassz egy felhasználót");
        RecyclerView rvUsers = dialog.findViewById(R.id.rvUsers);
        final UserListAdapter adapter = new UserListAdapter(list);
        rvUsers.setAdapter(adapter);
        LinearLayoutManager usersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(usersLayoutManager);
        dialog.show();

    }
}
