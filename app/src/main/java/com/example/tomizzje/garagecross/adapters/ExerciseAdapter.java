package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.activities.TimerActivity;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.models.FirebaseLogin;
import com.example.tomizzje.garagecross.models.FirebaseServer;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>{

    private final List<Exercise> exercises;

    public ExerciseAdapter(final List<Exercise> exercises) {

        this.exercises = exercises;

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
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public class ExerciseViewHolder extends RecyclerView.ViewHolder {

        /**
         * Fields connected by the view and strings.xml
         */

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.imgBtnStar)
        ImageButton imgBtnStar;

        @BindView(R.id.tvRate)
        TextView tvRate;

        @BindView(R.id.tvDifficulty)
        TextView tvDifficulty;

        @Inject
        FirebaseServer firebaseServer;

        @Inject
        FirebaseLogin firebaseLogin;

        @BindString(R.string.intent_bundle_key_select_exercise)
        String intentExerciseText;

        @BindString(R.string.database_reference_exercises)
        String exercisesReference;

        @BindString(R.string.exercise_adapter_added_favorites_toast)
        String addedFavoritesToast;

        @BindString(R.string.exercise_adapter_removed_favorites_toast)
        String removedFavoritesToast;

        @BindString(R.string.exercise_rated_no_data_text)
        String notRatedText;

        ExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            BaseApplication.getInstance().getBaseComponent().inject(this);
        }

        /**
         * this method set the onClickListener and view for each row of the list
         * @param exercise list element
         */
        public void bind(final Exercise exercise) {
            tvTitle.setText(exercise.getTitle());
            tvDifficulty.setText(Difficulty.getDifficultyByName(exercise.getDifficulty()).toString());

           if(exercise.getPopularity() != 0){
               String rate = String.format(Locale.getDefault(),"%.1f", exercise.getPopularity()) + "/5" ;

               tvRate.setText(rate);
           }else{
               tvRate.setText(notRatedText);
           }

            if(exercise.getFavoritedUsers() == null || !exercise.getFavoritedUsers().containsKey(firebaseLogin.getCurrentUser())){
                imgBtnStar.setImageResource(android.R.drawable.btn_star_big_off);
                exercise.setFavoritedUsers(new HashMap<String, String>());
            }else {
                imgBtnStar.setImageResource(android.R.drawable.btn_star_big_on);
            }

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Exercise selectedExercise = exercises.get(position);
                    Intent intent = new Intent(view.getContext(), TimerActivity.class);
                    intent.putExtra(intentExerciseText, selectedExercise);
                    view.getContext().startActivity(intent);
                }
            });

            imgBtnStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentUser = firebaseLogin.getCurrentUser();
                    if (!(exercise.getFavoritedUsers().containsKey(currentUser)) && currentUser != null) {
                        imgBtnStar.setImageResource(android.R.drawable.btn_star_big_on);

                        firebaseServer.updateFavoritesOfExercise(exercisesReference, exercise.getPushId(), firebaseLogin.getCurrentUser());
                        Toast.makeText(view.getContext(), addedFavoritesToast,
                                Toast.LENGTH_LONG).show();
                    } else {
                        imgBtnStar.setImageResource(android.R.drawable.btn_star_big_off);
                        firebaseServer.deleteFavoriteFromExercise(exercisesReference, exercise.getPushId(), firebaseLogin.getCurrentUser());
                        Toast.makeText(view.getContext(), removedFavoritesToast,
                                Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }
}
