package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.Model.Post;

public class AlbumHistoryActivity extends AppCompatActivity implements AlbumHistoryAdapter.OnAlbumListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    AlbumHistoryAdapter adapter;
    ArrayList<Post> list;
    FirebaseAuth mAuth;
    String babyid;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_history);

        // get current babyid
//        Intent intent = getIntent();
//        babyid = intent.getStringExtra("babyid");
        SharedPreferences babyPref = getSharedPreferences("babyInfo", MODE_PRIVATE);
        babyid = babyPref.getString("babyid", "");

        // get userid
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();

        recyclerView = findViewById(R.id.recyclerView_album);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        String path = "Posts/" + babyid;
        database = FirebaseDatabase.getInstance().getReference(path);
        list = new ArrayList<>();
        adapter = new AlbumHistoryAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (!post.getPostImages().equals("")) {
                        list.add(post);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onAlbumClick(int position) {
        Post post = list.get(position);

        Intent intent = new Intent(getApplicationContext(), AlbumFullImageActivity.class);
        intent.putExtra("babyid", babyid);
        intent.putExtra("postid", post.getPostid());
        intent.putExtra("postImages", post.getPostImages());
        intent.putExtra("publisher", post.getPublisher());

        startActivity(intent);
    }
}