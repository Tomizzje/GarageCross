package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.activities.TimerActivity;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.models.FirebaseLogin;
import com.example.tomizzje.garagecross.models.FirebaseServer;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.utils.ExerciseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> implements ValueEventListener {

    final ArrayList<Exercise> exercises;

    public ExerciseAdapter(final List<Exercise> exercises) {

        this.exercises = (ArrayList) exercises;

    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       Context context = viewGroup.getContext();
       View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_exercise, viewGroup,false);

       return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int i) {
        Exercise exercise = exercises.get(i);
        exerciseViewHolder.bind(exercise);
        //notifyDataSetChanged(); // itt elszáll ha ezt berakom

    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Log.d("READY2", "megváltozott");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        //@BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.imgButton) ImageButton imageButton;
        @BindView(R.id.tvRate) TextView tvRate;

        @Inject
        FirebaseServer firebaseServer;

        @Inject
        FirebaseLogin firebaseLogin;


        public ExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            BaseApplication.getInstance().getBaseComponent().inject(this);
        }

        public void bind(final Exercise exercise) {
            tvTitle.setText(exercise.getTitle());
            //tvDescription.setText(exercise.getDescription());

            if(exercise.getRatedUsers() == null) {
                tvRate.setText("N/A");
            } else {
                tvRate.setText(String.valueOf(ExerciseUtils.getRate(exercise))+ "/5.0");
            }

            if(exercise.getFavoritedUsers() == null || !exercise.getFavoritedUsers().containsKey(firebaseLogin.getCurrentUser())){
                imageButton.setImageResource(android.R.drawable.btn_star_big_off);
                exercise.setFavoritedUsers(new HashMap<String, String>());
            }else {
                imageButton.setImageResource(android.R.drawable.btn_star_big_on);
            }

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Exercise selectedExercise = exercises.get(position);
                    Intent intent = new Intent(view.getContext(), TimerActivity.class);
                    intent.putExtra("Exercise", selectedExercise);
                    view.getContext().startActivity(intent);
                }
            });



            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO
                    if (!(exercise.getFavoritedUsers().containsKey(firebaseLogin.getCurrentUser()))) {

                        imageButton.setImageResource(android.R.drawable.btn_star_big_on);

                        firebaseServer.updateFavoriteExercise("exercises", exercise.getPushId(), firebaseLogin.getCurrentUser());
                        Toast.makeText(view.getContext(), "Added to favourites!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        imageButton.setImageResource(android.R.drawable.btn_star_big_off);
                        firebaseServer.deleteFavoriteFromExercise("exercises", exercise.getPushId(), firebaseLogin.getCurrentUser());
                        Toast.makeText(view.getContext(), "Removed from favourites",
                                Toast.LENGTH_LONG).show();
                    }
                }

            });
        }


    }
}
