package edu.neu.madcourse.numad22sp_team5;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolders> {
    private ArrayList<Uri> uriArrayList;

    public RecyclerAdapter(ArrayList<Uri> uriArrayList){
        this.uriArrayList = uriArrayList;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_single_image,parent,false);
        return new ViewHolders(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolders holder, int position) {
        holder.imageView.setImageURI(uriArrayList.get(position));

    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolders extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolders(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
        }
    }
}
