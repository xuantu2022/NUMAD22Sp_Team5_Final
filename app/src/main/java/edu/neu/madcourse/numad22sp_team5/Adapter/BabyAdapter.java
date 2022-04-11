package edu.neu.madcourse.numad22sp_team5.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import edu.neu.madcourse.numad22sp_team5.Model.Baby;
import edu.neu.madcourse.numad22sp_team5.R;

public class BabyAdapter extends RecyclerView.Adapter<BabyAdapter.ViewHolder>{

    private List<Baby> babyList;
    private Context context;

    private OnBabyListener onBabyListener;

    public BabyAdapter(Context context, List<Baby> babyList, OnBabyListener onBabyListener) {
        this.context = context;
        this.babyList = babyList;
        this.onBabyListener = onBabyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.baby_item, parent, false);
        return new BabyAdapter.ViewHolder(view, onBabyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Baby baby = babyList.get(position);
        holder.babyName.setText(baby.getNickname());
        if (baby.getOwnerid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.babyOwner.setText("My");
        } else {
            holder.babyOwner.setVisibility(View.GONE);
        }
        Glide.with(context).load(baby.getHeadshot()).centerCrop().into(holder.headshot);
    }

    @Override
    public int getItemCount() {
        return babyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView headshot;
        private TextView babyName, babyOwner;
        private OnBabyListener onBabyListener;


        public ViewHolder(@NonNull View itemView, OnBabyListener onBabyListener) {
            super(itemView);

            headshot = itemView.findViewById(R.id.pic_baby);
            babyName = itemView.findViewById(R.id.name_baby);
            babyOwner = itemView.findViewById(R.id.baby_owner);
            this.onBabyListener = onBabyListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBabyListener.onBabyClick(getAdapterPosition());

        }
    }

    public interface OnBabyListener {
        void onBabyClick(int position);
    }
}
