package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomizzje.garagecross.models.DoneExercise;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoneExerciseAdapter extends RecyclerView.Adapter<DoneExerciseAdapter.DoneExerciseViewHolder> {

    final ArrayList<DoneExercise> doneExercises;

    private ChildEventListener childEventListener;

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

    public class DoneExerciseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDateTime) TextView tvDateTime;
        @BindView(R.id.tvElapsedTime) TextView tvElapsedTime;

        public DoneExerciseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DoneExercise doneExercise) {
            tvTitle.setText(doneExercise.getTitle());
            tvDateTime.setText(doneExercise.getDateTime());
            tvElapsedTime.setText(doneExercise.getTimeElapsed());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("CLICK",String.valueOf(position));
        }
    }

}
