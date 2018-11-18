package com.example.tomizzje.garagecross.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.activities.InsertFoodActivity;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.entities.Food;
import com.example.tomizzje.garagecross.models.FirebaseServer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> implements ValueEventListener {

    final ArrayList<Food> list;

    boolean isAdmin;


    public FoodAdapter(final List<Food> list, boolean isAdmin) {
        this.list = (ArrayList) list;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_food, viewGroup,false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i) {
        Food temp = list.get(i);
        foodViewHolder.bind(temp);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvFood) TextView tvFood;

        @Inject
        FirebaseServer firebaseServer;

        public FoodViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            BaseApplication.getInstance().getBaseComponent().inject(this);
        }

        public void bind(final Food temp) {
            if(temp !=null) {
                tvFood.setText(temp.getName());
            }

            if(isAdmin) {
                tvFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Food selectedFood = list.get(position);
                        Intent intent = new Intent(view.getContext(), InsertFoodActivity.class);
                        intent.putExtra("ModifyFood", selectedFood);
                        view.getContext().startActivity(intent);
                    }
                });
            }


        }



    }


}
