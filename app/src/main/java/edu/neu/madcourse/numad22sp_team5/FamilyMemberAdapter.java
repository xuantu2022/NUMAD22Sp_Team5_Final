package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.Model.User;

public class FamilyMemberAdapter extends RecyclerView.Adapter<FamilyMemberAdapter.FamilyMemberViewHolder> {
    Context context;
    ArrayList<User> list;
    FirebaseUser firebaseUser;

    public FamilyMemberAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FamilyMemberAdapter.FamilyMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family, parent, false);
        return new FamilyMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyMemberAdapter.FamilyMemberViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        User user = list.get(position);
        holder.family_username.setText(user.getUsername());
        holder.family_email.setText(user.getEmail());

        if (user.getId() != null && user.getId().equals(firebaseUser.getUid())) {
            Log.i("db-debug", "a me:" + user.getId());
            holder.family_me.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView family_username;
        TextView family_email;
        TextView family_me;

        public FamilyMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            family_username = itemView.findViewById(R.id.textView_family_username);
            family_email = itemView.findViewById(R.id.textView_family_email);
            family_me = itemView.findViewById(R.id.textView_family_me);
        }
    }
}
