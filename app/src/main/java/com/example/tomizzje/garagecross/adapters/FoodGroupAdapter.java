package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.entities.Food;
import com.example.tomizzje.garagecross.enums.FoodGroup;
import com.example.tomizzje.garagecross.models.FirebaseLogin;
import com.example.tomizzje.garagecross.models.FirebaseServer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodGroupAdapter extends RecyclerView.Adapter<FoodGroupAdapter.FoodGroupViewHolder> {

    private final List<FoodGroup> list;

    private final boolean[] clicks;

    public FoodGroupAdapter(final List<FoodGroup> list) {
        this.list =  list;
        this.clicks = new boolean[list.size()];
    }

    @NonNull
    @Override
    public FoodGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_food_groups, viewGroup,false);
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

        @Inject
        FirebaseServer firebaseServer;

        @Inject
        FirebaseLogin firebaseLogin;

        @BindView(R.id.tvFood)
        TextView tvFood;

        @BindView(R.id.btnExpand)
        ImageButton btnExpand;

        @BindView(R.id.rvFood)
        RecyclerView rvFood;

        @BindString(R.string.intent_bundle_key_select_foodGroup)
        String intentFoodGroupText;

        @BindString(R.string.database_reference_administrators)
        String administratorsReference;

        private boolean isAdmin = false;

        FoodGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            BaseApplication.getInstance().getBaseComponent().inject(this);
        }

        public void bind(final FoodGroup temp) {
            final int position = getAdapterPosition();
            initAdministrator();
            clicks[position] = false;
            rvFood.setVisibility(View.GONE);
            if(temp !=null) {
                tvFood.setText(temp.toString());
            }

            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFoodList(getAdapterPosition(), temp);
                }
            });

            tvFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFoodList(getAdapterPosition(), temp);
                }
            });
        }

        private void initFoodList(final FoodGroup temp) {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        ArrayList<Food> food = new ArrayList<>();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(temp != null && temp.name().equals(snapshot.getValue(Food.class).getFoodGroups())){
                                food.add(snapshot.getValue(Food.class));
                            }
                        }
                        initAdapter(food, isAdmin);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            firebaseServer.findItemsOfNode(valueEventListener, "food");
        }

        private void initAdapter(ArrayList<Food> food, boolean isAdmin) {
            final FoodAdapter adapter = new FoodAdapter(food, isAdmin);
            rvFood.setAdapter(adapter);
            LinearLayoutManager foodLayoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            rvFood.setLayoutManager(foodLayoutManager);
            rvFood.setVisibility(View.VISIBLE);
        }


        private void initAdministrator() {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals(firebaseLogin.getCurrentUser())){
                                isAdmin = true;

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            firebaseServer.findItemsOfNode(valueEventListener,administratorsReference);
        }

        private void showFoodList(int position, FoodGroup temp){
            if(!clicks[position]){
                initFoodList(temp);
                clicks[position] = true;
            }else{
                rvFood.setVisibility(View.GONE);
                clicks[position] = false;
            }

        }

    }


}
