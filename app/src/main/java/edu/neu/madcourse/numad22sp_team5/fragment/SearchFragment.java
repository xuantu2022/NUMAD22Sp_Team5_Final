package edu.neu.madcourse.numad22sp_team5.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.neu.madcourse.numad22sp_team5.FamilySearchAdapter;
import edu.neu.madcourse.numad22sp_team5.Model.User;
import edu.neu.madcourse.numad22sp_team5.R;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView_family_search;
    private DatabaseReference database;
    private FamilySearchAdapter familySearchAdapter;
    private List<User> users;
    private Map<String, User> userMapping;

    EditText search_bar;
    String babyid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get baby id by intent
        babyid = getActivity().getIntent().getExtras().getString("babyid");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView_family_search = view.findViewById(R.id.recyclerView_family_search);
        recyclerView_family_search.setHasFixedSize(true);
        recyclerView_family_search.setLayoutManager(new LinearLayoutManager(getContext()));

        search_bar = view.findViewById(R.id.search_bar);
        users = new ArrayList<>();
        familySearchAdapter = new FamilySearchAdapter(getContext(), users, babyid);
        recyclerView_family_search.setAdapter(familySearchAdapter);

        readFollowingUsers(babyid);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    users.add(user);
                }
                familySearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readFollowingUsers(String babyid) {
        userMapping = new HashMap<>();
        database = FirebaseDatabase.getInstance().getReference("Users");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (search_bar.getText().toString().equals("")) {
                    users.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        userMapping.put(user.getId(), user);
                    }
                    for (String id : userMapping.keySet()) {
                        findFollowers(id);
                    }
                    familySearchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // find users that follow current baby
    private void findFollowers(String id) {
        database = FirebaseDatabase.getInstance().getReference("Follow");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                if (snapshot.child(id).exists()) { // check if user exists in Follow branch
                    // check if user follow current baby
                    database = FirebaseDatabase.getInstance().getReference("Follow/" + id);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child(babyid).exists()) {
                                users.add(userMapping.get(id));
                            }
                            familySearchAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                familySearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}