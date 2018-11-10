package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.models.User;
import com.example.tomizzje.garagecross.utils.UserUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WelcomeActivity extends MenuBaseActivity implements ValueEventListener {

    @BindView(R.id.tvWelcome) TextView tvWelcome;

    @BindView(R.id.btnGoodDay) Button btnGoodDay;

    @BindView(R.id.tvLevel) TextView tvLevel;

    @BindView(R.id.pgsBar) ProgressBar pgsBar;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);



    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("HEYHOKA", "onresume");
        firebaseLogin.detachListener();
        firebaseLogin.checkLogin(this);
        firebaseLogin.attachListener();

        initProgressBar();
        if(firebaseLogin.isSignedIn()) {


            String email = firebaseLogin.getEmail();
            String name = firebaseLogin.getName();
            String id = firebaseLogin.getCurrentUser();
            String welcome = "Üdvözöllek " + name + "!";
            tvWelcome.setText(welcome);

            this.user = new User(id, email, name, 0, "defaultId");
            firebaseServer.findAll(this, "users");

            initButtonGoodDay();


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("HEYHOKA", "onpause");
        firebaseLogin.detachListener();
    }

    private void initProgressBar() {
        pgsBar.setMax(400);
        pgsBar.setProgress(0);
    }



    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // TODO

        if(dataSnapshot.exists() ) {

            List<User> users = new ArrayList<>();
            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                users.add(snapshot.getValue(User.class));
            }

            boolean gotIt = false;
            for(User u : users) {
                if(u.getUser_id().equals(this.user.getUser_id())) {
                    gotIt = true;
                    this.user = u;
                }
            }
            if(gotIt) {
                Log.d("EMAIL:" , this.user.getEmail());
                String level = UserUtils.getLevelByExperience(user.getExperience());
                String msg = "A jelenlegi szinted:" + level + " " + user.getExperience() +  " pont";
                tvLevel.setText(msg);
                pgsBar.setProgress(this.user.getExperience());

            } else {
                firebaseServer.insertUser(this.user, "users");
            }


        }

    }



    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    private void initButtonGoodDay() {
        btnGoodDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            List<Exercise> exercises = new ArrayList<>();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                exercises.add(snapshot.getValue(Exercise.class));
                            }
                            Exercise selectedExercise = getRandomExercise(exercises);
                            Intent intent = new Intent(view.getContext(), TimerActivity.class);
                            intent.putExtra("Exercise", selectedExercise);
                            view.getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                firebaseServer.findAll(valueEventListener, "exercises");
            }
        });

    }

    private Exercise getRandomExercise(List<Exercise> exercises ) {
        Random random = new Random();
        return exercises.get(random.nextInt(exercises.size()));
    }



}
