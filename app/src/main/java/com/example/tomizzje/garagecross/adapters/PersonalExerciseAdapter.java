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

import com.example.tomizzje.garagecross.activities.InsertExerciseActivity;
import com.example.tomizzje.garagecross.activities.TimerActivity;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalExerciseAdapter extends RecyclerView.Adapter<PersonalExerciseAdapter.PersonalExerciseViewHolder> {

    private final List<Exercise> personalExercises;

    public PersonalExerciseAdapter(final List<Exercise> personalExercises) {
        this.personalExercises =  personalExercises;
    }

    @NonNull
    @Override
    public PersonalExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_personal_exercise, viewGroup,false);
        return new PersonalExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonalExerciseViewHolder personalExerciseViewHolder, int i) {
        Exercise personalExercise = personalExercises.get(i);
        personalExerciseViewHolder.bind(personalExercise);
    }

    @Override
    public int getItemCount() {
        return personalExercises.size();
    }

    public class PersonalExerciseViewHolder extends RecyclerView.ViewHolder{

        /**
         * Fields connected by the view and strings.xml
         */

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvRate)
        TextView tvRate;

        @BindView(R.id.tvDifficulty)
        TextView tvDifficulty;

        @BindView(R.id.imgButtonModify)
        ImageButton imgButtonModify;

        @BindString(R.string.intent_bundle_key_modify_exercise)
        String intentModifyExercise;

        @BindString(R.string.intent_bundle_key_select_exercise)
        String intentExerciseText;

        @BindString(R.string.exercise_rated_no_data_text)
        String notRatedText;

        PersonalExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Exercise personalExercise) {

            tvTitle.setText(personalExercise.getTitle());

            if(personalExercise.getPopularity() != 0){
                String rate = String.format(Locale.getDefault(),"%.1f", personalExercise.getPopularity()) + "/5" ;
                //String rate = String.valueOf(personalExercise.getPopularity()) + "/5";
                tvRate.setText(rate);
            }else{
                tvRate.setText(notRatedText);
            }
            tvDifficulty.setText(Difficulty.getDifficultyByName(personalExercise.getDifficulty()).toString());
            final Exercise selectedExercise = personalExercises.get(getAdapterPosition());
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TimerActivity.class);
                    intent.putExtra(intentExerciseText, selectedExercise);
                    view.getContext().startActivity(intent);
                }
            });

            imgButtonModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), InsertExerciseActivity.class);
                    intent.putExtra(intentModifyExercise, selectedExercise);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }

}
