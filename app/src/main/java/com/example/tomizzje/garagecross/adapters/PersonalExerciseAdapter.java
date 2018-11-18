package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomizzje.garagecross.activities.InsertExerciseActivity;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.enums.Difficulty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalExerciseAdapter extends RecyclerView.Adapter<PersonalExerciseAdapter.PersonalExerciseViewHolder> {

    final ArrayList<Exercise> personalExercises;

    public PersonalExerciseAdapter(final List<Exercise> personalExercises) {
        this.personalExercises = (ArrayList) personalExercises;
    }

    @NonNull
    @Override
    public PersonalExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        //TODO
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

    public class PersonalExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //TODO
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvRate)
        TextView tvRate;

        @BindView(R.id.tvDifficulty)
        TextView tvDifficulty;

        public PersonalExerciseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Exercise personalExercise) {
            //todo
            tvTitle.setText(personalExercise.getTitle());
            String rateText = String.valueOf(personalExercise.getPopularity()) + "/5";
            tvRate.setText(rateText);
            tvDifficulty.setText(Difficulty.getDifficultyByName(personalExercise.getDifficulty()).toString());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Exercise selectedExercise = personalExercises.get(position);
            Intent intent = new Intent(view.getContext(), InsertExerciseActivity.class);
            intent.putExtra("ModifyExercise", selectedExercise);
            view.getContext().startActivity(intent);
        }
    }

}
