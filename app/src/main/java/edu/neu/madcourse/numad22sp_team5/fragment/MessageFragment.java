package edu.neu.madcourse.numad22sp_team5.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.numad22sp_team5.Adapter.ItemMessage;
import edu.neu.madcourse.numad22sp_team5.Adapter.ItemMessageAdapter;
import edu.neu.madcourse.numad22sp_team5.R;


public class MessageFragment extends Fragment {
    private ArrayList<ItemMessage> messages = new ArrayList<>();

    private RecyclerView messageView;
    private ItemMessageAdapter messageAdapter;
    private RecyclerView.LayoutManager rLayoutManger;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        createMessageView(view);

        readNotification();
        // addMessage(0, "System notification", "nickname");
        return view;
    }

    // Generates a list of babies that the user is following.
    private void readNotification() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    boolean follow = (boolean) snapshot.getValue();
                    if (follow) {
                        String baby_id = snapshot.getKey();
                        String nickname = baby_id;
                        messages.add(0, new ItemMessage(baby_id, nickname));
                    }
                }

                Collections.reverse(messages);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createMessageView(View view) {
        messageView = view.findViewById(R.id.messages);
        messageView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(getContext());
        messageView.setLayoutManager(rLayoutManger);
        messageAdapter = new ItemMessageAdapter(getContext(), messages);
        messageView.setAdapter(messageAdapter);
    }

    private void addMessage(int position, String babyId, String nickname) {
        messages.add(position, new ItemMessage(babyId, nickname));
        messageAdapter.notifyItemInserted(position);
    }
}