package com.example.tomizzje.garagecross.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.application.BaseApplication;
import com.example.tomizzje.garagecross.events.MessageEvent;
import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.models.FirebaseLogin;
import com.example.tomizzje.garagecross.models.FirebaseServer;
import com.example.tomizzje.garagecross.entities.Share;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private final List<Share> shares;

    public ShareAdapter(final List<Share> shares) {
        this.shares = shares;
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row_share, viewGroup,false);
        return new ShareViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder shareViewHolder, int i) {
        Share share  = shares.get(i);
        shareViewHolder.bind(share);
    }

    @Override
    public int getItemCount() {
        return shares.size();
    }

    public class ShareViewHolder extends RecyclerView.ViewHolder {

        @Inject
        FirebaseServer firebaseServer;

        @BindView(R.id.imgShare)
        ImageView imgShare;

        @BindView(R.id.tvUser)
        TextView tvUser;

        @BindView(R.id.tvDoneExerciseTitle)
        TextView tvDoneExerciseTitle;

        @BindView(R.id.tvElapsedTime)
        TextView tvElapsedTime;

        @BindView(R.id.tvComment)
        TextView tvComment;

        @BindView(R.id.tvShareDate)
        TextView tvShareDate;

        @BindView(R.id.imgDelete)
        ImageView imgDelete;

        @BindString(R.string.database_reference_shares)
        String sharesReference;

        @BindString(R.string.share_adapter_share_removed_toast)
        String shareRemovedToast;


        ShareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            BaseApplication.getInstance().getBaseComponent().inject(this);
        }

        public void bind(final Share share) {
            String nameText = share.getDoneExercise().getUser().getName() + " -";
            tvUser.setText(nameText);
            tvDoneExerciseTitle.setText(share.getDoneExercise().getTitle());
            tvElapsedTime.setText(share.getDoneExercise().getTimeElapsed());
            tvComment.setText(share.getComment());
            tvShareDate.setText(share.getDateTime());

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), shareRemovedToast,
                            Toast.LENGTH_LONG).show();
                    firebaseServer.deleteEntity(share, sharesReference);

                }
            });
        }

    }

}
