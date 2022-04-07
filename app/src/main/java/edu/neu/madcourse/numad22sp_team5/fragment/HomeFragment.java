package edu.neu.madcourse.numad22sp_team5.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.numad22sp_team5.Model.Post;
import edu.neu.madcourse.numad22sp_team5.Adapter.PostAdapter;
import edu.neu.madcourse.numad22sp_team5.R;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;

    //private List<String> followingList;

    private String babyid;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Set icon for FBA
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setImageResource(R.drawable.ic_add_items);

        //add background pic for home fragment
        StorageReference backgroundRef = FirebaseStorage.getInstance().getReference().child("app_materials/bg1.jpg");
        StorageReference headshotRef = FirebaseStorage.getInstance().getReference().child("app_materials/baby1.jpeg");
        ImageView background = view.findViewById(R.id.background);
        ImageView headshot = view.findViewById(R.id.headshot);
        //center crop crop picture based on imageview size
        Glide.with(this).load(backgroundRef).centerCrop().into(background);
        Glide.with(this).load(headshotRef).centerCrop().into(headshot);

        recyclerView = view.findViewById(R.id.post_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists);
        recyclerView.setAdapter(postAdapter);

        //checkFollowing();


        //get value pass from activity use bundle or create a method
        //Bundle bundle = new Bundle();
        //bundle.putString("babyid", "baby01");
        //// set Fragmentclass Arguments
        //Fragmentclass fragobj = new Fragmentclass();
        //fragobj.setArguments(bundle);
        //https://stackoverflow.com/questions/12739909/send-data-from-activity-to-fragment-in-android

        // disable for temp
        // babyid = getArguments().getString("babyid");
        readPosts();

        // Inflate the layout for this fragment
        return view;
    }

    //do not need this part
    /*
    private void checkFollowing() {
        followingList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for(DataSnapshot data: snapshot.getChildren()) {
                    followingList.add(data.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

    //add a babyId layer, should come from babyList Intent
    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts")
                .child("baby01");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postLists.clear();
                for(DataSnapshot data : snapshot.getChildren()) {
                    Post post = data.getValue(Post.class);
                    //for(String id: followingList) {
                    //    if (post.getPublisher().equals(id)) {
                            postLists.add(post);
                    //    }
                    //}
                }
                for (Post post: postLists) {
                    Log.d("post", post.getTag());
                    Log.d("post", post.getDescription());
                    Log.d("post", post.getPostImages());
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




}