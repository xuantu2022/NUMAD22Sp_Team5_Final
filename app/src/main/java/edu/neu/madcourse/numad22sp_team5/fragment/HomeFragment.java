package edu.neu.madcourse.numad22sp_team5.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import edu.neu.madcourse.numad22sp_team5.AlbumHistoryActivity;
import edu.neu.madcourse.numad22sp_team5.BabyListActivity;
import edu.neu.madcourse.numad22sp_team5.FamilyPageActivity;
import edu.neu.madcourse.numad22sp_team5.GrowthHistoryActivity;
import edu.neu.madcourse.numad22sp_team5.MainActivity;
import edu.neu.madcourse.numad22sp_team5.MilestoneHistoryActivity;
import edu.neu.madcourse.numad22sp_team5.MainActivity;
import edu.neu.madcourse.numad22sp_team5.Model.Post;
import edu.neu.madcourse.numad22sp_team5.Adapter.PostAdapter;
import edu.neu.madcourse.numad22sp_team5.PostDetailActivity;
import edu.neu.madcourse.numad22sp_team5.AddItemActivity;
import edu.neu.madcourse.numad22sp_team5.R;
import edu.neu.madcourse.numad22sp_team5.Support.ThroughTimeLineDecorator;



public class HomeFragment extends Fragment implements PostAdapter.OnPostListener {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postLists;
    private TextView babyName;

    private String babyid;
    private String babyHeadshot;
    private String nickname;
    FloatingActionButton floatingActionButton;


    private TextView home_growth;
    private TextView home_milestone;
    private TextView home_family;
    private TextView home_album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences pref = requireActivity().getSharedPreferences("babyInfo",MODE_PRIVATE);
        babyid = pref.getString("babyid","");
        babyHeadshot = pref.getString("headshot", "");
        nickname = pref.getString("nickname", "");

        //get babyid and baby headshot from baby list
        /*
        MainActivity mainActivity = (MainActivity) getActivity();
        babyid = mainActivity.getBabyid();

        babyHeadshot = mainActivity.getHeadshot();
        nickname = mainActivity.getNickname();*/

        //set baby name
        babyName = view.findViewById(R.id.baby_name);
        babyName.setText(nickname);

        // add onclick for history display
        home_growth = view.findViewById(R.id.growth);
        home_growth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GrowthHistoryActivity.class);
                //intent.putExtra("babyid", babyid);
                startActivity(intent);
            }
        });

        home_milestone = view.findViewById(R.id.milestone);
        home_milestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MilestoneHistoryActivity.class);
                //intent.putExtra("babyid", babyid);
                startActivity(intent);
            }
        });

        home_family = view.findViewById(R.id.family);
        home_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FamilyPageActivity.class);
                //intent.putExtra("babyid", babyid);
                startActivity(intent);
            }
        });

        home_album = view.findViewById(R.id.album);
        home_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AlbumHistoryActivity.class);
                //intent.putExtra("babyid", babyid);
                startActivity(intent);
            }
        });



        //Set icon for FBA

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setImageResource(R.drawable.ic_add);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddItemActivity();

            }
        });

        //add background pic for home fragment
        //StorageReference backgroundRef = FirebaseStorage.getInstance().getReference().child("app_materials/bg1.jpg");
        ImageView background = view.findViewById(R.id.background);
        background.setImageResource(R.drawable.bg);
        //Glide.with(this).load(backgroundRef).centerCrop().into(background);

        //set baby headshot
        ImageView headshot = view.findViewById(R.id.headshot);
        //center crop crop picture based on imageview size
        Glide.with(this).load(babyHeadshot).centerCrop().into(headshot);

        recyclerView = view.findViewById(R.id.post_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //set timeline decoration for recycler view with two drawable source dot.xml and shape_line.xml
        //Decoration use a resource class ThroughTimeLineDecorator
        recyclerView.addItemDecoration(new ThroughTimeLineDecorator(ResourcesCompat.getDrawable(getResources(), R.drawable.dot, null),
                ResourcesCompat.getDrawable(getResources(), R.drawable.shape_line, null),10, 5, 15));

        postLists = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postLists, this);
        recyclerView.setAdapter(postAdapter);


        readPosts();

        // Inflate the layout for this fragment
        return view;
    }


    //add a babyId layer, should come from babyList Intent
    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts")
                .child(babyid);

        Query postsQuery = FirebaseDatabase.getInstance().getReference().child("Posts").child(babyid).orderByChild("time");

        postsQuery.addValueEventListener(new ValueEventListener() {
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
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void openAddItemActivity(){
        Intent intent = new Intent(getActivity(),AddItemActivity.class);
        intent.putExtra("babyid",babyid);
        startActivity(intent);
    }



    @Override
    public void onPostClick(int position) {
        /*
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("postInfo", MODE_PRIVATE).edit();
        editor.putString("postid", postLists.get(position).getPostid());
        editor.putString("publisherid", postLists.get(position).getPublisher());
        editor.commit();*/

        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("postid", postLists.get(position).getPostid());
        intent.putExtra("babyid", postLists.get(position).getBabyid());
        intent.putExtra("publisherid", postLists.get(position).getPublisher());
        startActivity(intent);
    }
}