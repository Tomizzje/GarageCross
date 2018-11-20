package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomizzje.garagecross.activities.InsertShareActivity;
import com.example.tomizzje.garagecross.entities.DoneExercise;
import com.example.tomizzje.garagecross.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DoneExerciseAdapter extends RecyclerView.Adapter<DoneExerciseAdapter.DoneExerciseViewHolder> {

    final ArrayList<DoneExercise> doneExercises;

    public DoneExerciseAdapter(final List<DoneExercise> doneExercises) {

        this.doneExercises = (ArrayList) doneExercises;
    }

    @NonNull
    @Override
    public DoneExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       Context context = viewGroup.getContext();
       View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_done_exercise, viewGroup,false);
       return new DoneExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneExerciseViewHolder doneExerciseViewHolder, int i) {
        DoneExercise doneExercise = doneExercises.get(i);
        doneExerciseViewHolder.bind(doneExercise);
    }

    @Override
    public int getItemCount() {
        return doneExercises.size();
    }

    public class DoneExerciseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvDateTime)
        TextView tvDateTime;

        @BindView(R.id.tvElapsedTime)
        TextView tvElapsedTime;

        @BindView(R.id.imgShare)
        ImageView imgShare;

        @BindString(R.string.intent_bundle_key_select_doneExercise)
        String intentDoneExerciseText;

        public DoneExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DoneExercise doneExercise) {
            tvTitle.setText(doneExercise.getTitle());
            tvDateTime.setText(doneExercise.getDateTime());
            tvElapsedTime.setText(doneExercise.getTimeElapsed());

            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    DoneExercise selectedExercise = doneExercises.get(position);
                    Intent intent = new Intent(view.getContext(), InsertShareActivity.class);
                    intent.putExtra(intentDoneExerciseText, selectedExercise);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }

}
