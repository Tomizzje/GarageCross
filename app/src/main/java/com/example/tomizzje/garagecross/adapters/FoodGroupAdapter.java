package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.activities.FoodListActivity;
import com.example.tomizzje.garagecross.enums.FoodGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodGroupAdapter extends RecyclerView.Adapter<FoodGroupAdapter.FoodGroupViewHolder> {

    final ArrayList<FoodGroup> list;


    public FoodGroupAdapter(final List<FoodGroup> list) {
        this.list = (ArrayList) list;
    }

    @NonNull
    @Override
    public FoodGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_food, viewGroup,false);
        return new FoodGroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodGroupViewHolder foodViewHolder, int i) {
        FoodGroup temp = list.get(i);
        foodViewHolder.bind(temp);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FoodGroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvFood) TextView tvFood;

        public FoodGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final FoodGroup temp) {
            if(temp !=null) {
                tvFood.setText(temp.toString());
            }

            tvFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    FoodGroup selectedFoodGroup = list.get(position);
                    Intent intent = new Intent(view.getContext(), FoodListActivity.class);
                    intent.putExtra("FoodGroup", selectedFoodGroup);
                    view.getContext().startActivity(intent);
                }
            });

        }



    }


}
