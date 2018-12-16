package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodGroupAdapter extends RecyclerView.Adapter<FoodGroupAdapter.FoodGroupViewHolder> {

    private final List<FoodGroup> foodGroupList;

    private final boolean[] clicks;

    public FoodGroupAdapter(final List<FoodGroup> foodGroupList) {
        this.foodGroupList = foodGroupList;
        this.clicks = new boolean[foodGroupList.size()];
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
        FoodGroup temp = foodGroupList.get(i);
        foodViewHolder.bind(temp);
    }

    @Override
    public int getItemCount() {
        return foodGroupList.size();
    }

    public class FoodGroupViewHolder extends RecyclerView.ViewHolder{

        /**
         * Fields connected by the view and strings.xml
         */

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

        @BindString(R.string.database_reference_food)
        String foodReference;

        private boolean isAdmin = false;

        FoodGroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            BaseApplication.getInstance().getBaseComponent().inject(this);
        }

        /**
         * this method set the onClickListener and view for each row of the list
         * @param foodGroup list element
         */

        public void bind(final FoodGroup foodGroup) {
            final int position = getAdapterPosition();
            initAdministrator();
            clicks[position] = false;
            rvFood.setVisibility(View.GONE);
            if(foodGroup !=null) {
                tvFood.setText(foodGroup.toString());
            }

            btnExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFoodList(getAdapterPosition(), foodGroup);
                }
            });

            tvFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFoodList(getAdapterPosition(), foodGroup);
                }
            });
        }

        private void initFoodList(final FoodGroup foodGroup) {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        ArrayList<Food> food = new ArrayList<>();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(foodGroup != null && foodGroup.name().equals(snapshot.getValue(Food.class).getFoodGroups())){
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
            firebaseServer.findItemsOfNode(valueEventListener, foodReference);
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
                btnExpand.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24px);
                initFoodList(temp);
                clicks[position] = true;
            }else{
                btnExpand.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24px);
                rvFood.setVisibility(View.GONE);
                clicks[position] = false;
            }

        }

    }


}
