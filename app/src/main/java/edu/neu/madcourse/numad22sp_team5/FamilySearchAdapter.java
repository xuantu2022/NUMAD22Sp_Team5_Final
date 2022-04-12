package edu.neu.madcourse.numad22sp_team5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import edu.neu.madcourse.numad22sp_team5.Model.User;

public class FamilySearchAdapter extends RecyclerView.Adapter<FamilySearchAdapter.FamilySearchViewHolder> {
    Context context;
    List<User> list;

    FirebaseUser firebaseUser;
    String babyid;

    public FamilySearchAdapter(Context context, List<User> list, String babyid) {
        this.context = context;
        this.list = list;
        this.babyid = babyid;
    }

    @NonNull
    @Override
    public FamilySearchAdapter.FamilySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family_search, parent, false);
        return new FamilySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilySearchAdapter.FamilySearchViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = list.get(position);
        holder.button_follow.setVisibility(View.VISIBLE);
        holder.search_username.setText(user.getUsername());
        isFollowing(user.getId(), holder.button_follow);
        // do not show button for current user
        if (user.getId().equals(firebaseUser.getUid())) {
            holder.button_follow.setVisibility(View.GONE);
        }
        holder.button_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.button_follow.getText().toString().equals("add")) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child(babyid).setValue(true);
                    //FirebaseDatabase.getInstance().getReference().child("Follow").child("user01").child(babyId).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child(babyid).removeValue();
                    //FirebaseDatabase.getInstance().getReference().child("Follow").child("user01").child(babyId).removeValue();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FamilySearchViewHolder extends RecyclerView.ViewHolder{
        TextView search_username;
        Button button_follow;

        public FamilySearchViewHolder(@NonNull View itemView) {
            super(itemView);
            search_username = itemView.findViewById(R.id.search_username);
            button_follow = itemView.findViewById(R.id.button_follow);
        }
    }

    private void isFollowing(final String userid, final Button button) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(userid);
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child("user01");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(babyid).exists()) {
                    button.setText("remove");
                } else {
                    button.setText("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
