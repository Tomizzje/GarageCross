package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.tomizzje.garagecross.activities.InsertRecordActivity;
import com.example.tomizzje.garagecross.entities.Record;
import com.example.tomizzje.garagecross.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private final List<Record> records;

    public RecordAdapter(final List<Record> records) {
        this.records =  records;
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

    public class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvDescription)
        TextView tvDescription;

        @BindString(R.string.intent_bundle_key_modify_record)
        String intentModifyRecord;

        RecordViewHolder(View itemView) {
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
            intent.putExtra(intentModifyRecord, selectedRecord);
            view.getContext().startActivity(intent);
        }
    }

}
