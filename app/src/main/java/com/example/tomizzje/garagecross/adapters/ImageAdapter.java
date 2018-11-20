package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.tomizzje.garagecross.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImagesViewHolder> {

    final ArrayList<String> list;


    public ImageAdapter(final List<String> list) {
        this.list = (ArrayList) list;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_image, viewGroup,false);
        return new ImagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder imagesViewHolder, int i) {
        String temp = list.get(i);
        imagesViewHolder.bind(temp);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgExercise) ImageView imgExercise;

        public ImagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final String temp) {
            if(temp != null && !temp.isEmpty()){
                Picasso.get().load(temp).resize(300,300).centerCrop().into(imgExercise);
            }

            imgExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup(view, temp);
                }
            });

        }

        private void showPopup(View view, final String temp) {
            final ImagePopup imagePopup = new ImagePopup(view.getContext());
            imagePopup.setImageOnClickClose(true);
            imagePopup.initiatePopupWithGlide(temp);
            imagePopup.viewPopup();
        }

    }


}
