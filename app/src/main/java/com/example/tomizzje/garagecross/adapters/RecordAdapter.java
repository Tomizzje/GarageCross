package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.tomizzje.garagecross.activities.InsertRecordActivity;
import com.example.tomizzje.garagecross.activities.TimerActivity;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.models.Record;
import com.example.tomizzje.garagecross.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> implements ValueEventListener {

    final ArrayList<Record> records;

    public RecordAdapter(final List<Record> records) {

        this.records = (ArrayList) records;

    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_record, viewGroup,false);
        return new RecordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder recordViewHolder, int i) {
        Record record = records.get(i);
        recordViewHolder.bind(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvDate) TextView tvDate;
        @BindView(R.id.tvDescription) TextView tvDescription;

        public RecordViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Record record) {
            tvDate.setText(record.getDate());
            tvDescription.setText(record.getDescription());

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Record selectedRecord = records.get(position);
            Intent intent = new Intent(view.getContext(), InsertRecordActivity.class);
            intent.putExtra("ModifyRecord", selectedRecord);
            view.getContext().startActivity(intent);
        }
    }

}
