package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomizzje.garagecross.events.MessageEvent;
import com.example.tomizzje.garagecross.entities.User;
import com.example.tomizzje.garagecross.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    final ArrayList<User> users;
    public UserListAdapter(final List<User> users) {

        this.users = (ArrayList) users;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       Context context = viewGroup.getContext();
       View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_user, viewGroup,false);
       return new UserListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder userListViewHolder, int i) {
        User user = users.get(i);
        userListViewHolder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvEmail) TextView tvEmail;

        public UserListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(User user) {
           tvEmail.setText(user.getEmail());
           tvName.setText(user.getName());

           tvName.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getAdapterPosition();
                   User selectedUser = users.get(position);
                   EventBus.getDefault().post(new MessageEvent(selectedUser));
               }
           });
        }

    }

}
