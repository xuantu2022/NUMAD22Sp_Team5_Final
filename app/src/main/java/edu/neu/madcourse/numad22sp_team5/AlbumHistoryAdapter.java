package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.Model.Post;

public class AlbumHistoryAdapter extends RecyclerView.Adapter<AlbumHistoryAdapter.AlbumHistoryViewHolder> {

    Context context;
    ArrayList<Post> list;
    OnAlbumListener listener;


    public AlbumHistoryAdapter(Context context, ArrayList<Post> list, OnAlbumListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumHistoryAdapter.AlbumHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumHistoryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHistoryAdapter.AlbumHistoryViewHolder holder, int position) {
        Post albumPost = list.get(position);
        String url = albumPost.getPostImages();
        Glide.with(context).load(url).centerCrop().into(holder.roundedImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AlbumHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RoundedImageView roundedImageView;
        OnAlbumListener onAlbumListener;

        public AlbumHistoryViewHolder(@NonNull View itemView, OnAlbumListener onAlbumListener) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.roundedImageView_album);
            this.onAlbumListener = onAlbumListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onAlbumListener.onAlbumClick(getAdapterPosition());
        }
    }

    public interface OnAlbumListener {
        void onAlbumClick(int position);
    }
}
