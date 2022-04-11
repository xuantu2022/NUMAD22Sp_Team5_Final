package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.neu.madcourse.numad22sp_team5.Adapter.CommentAdapter;
import edu.neu.madcourse.numad22sp_team5.Model.Comment;
import edu.neu.madcourse.numad22sp_team5.Model.Post;
import edu.neu.madcourse.numad22sp_team5.Model.User;

public class PostDetailActivity extends AppCompatActivity {
    Button sendComment;
    EditText addComment;

    String postid;
    String publisherid;
    String babyid;
    Post post;

    FirebaseUser firebaseUser;

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    public ImageView post_image, like, comment;
    public TextView publisher, description, growth, tag, publish_time;
    public LinearLayout growth_holder, description_holder, tag_holder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        /*
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);*/

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        babyid = intent.getStringExtra("babyid");
        publisherid = intent.getStringExtra("publisherid");

        recyclerView = findViewById(R.id.comments_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(commentAdapter);

        post_image = findViewById(R.id.post_detail_image);
        like = findViewById(R.id.post_detail_like);
        comment = findViewById(R.id.post_detal_comment);

        publisher = findViewById(R.id.post_detail_publisher);
        description = findViewById(R.id.post_detail_description);

        growth = findViewById(R.id.post_detail_growth);
        tag = findViewById(R.id.post_detail_tag);
        publish_time = findViewById(R.id.post_detail_time);

        //LinearLayout to hide info
        tag_holder = findViewById(R.id.post_detail_tag_holder);
        growth_holder = findViewById(R.id.post_detail_growth_holder);
        description_holder = findViewById(R.id.post_detail_description_holder);

        addComment = findViewById(R.id.add_comment);
        sendComment = findViewById(R.id.send_comment);
        addComment = findViewById(R.id.add_comment);

        isLiked(postid,like);

        readPost(postid, growth_holder, tag_holder, description_holder,  growth, tag,
                description,  post_image,  publish_time, publisher);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment.requestFocus();
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(PostDetailActivity.this, "Please say something.", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(like.getTag().equals("like")) {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(postid)
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        readComments();


        //To display growth


    }

    private void isLiked(String postid, ImageView imageView) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_liked);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addComment() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        HashMap<String, Object> map = new HashMap<>();
        map.put("comment", addComment.getText().toString());
        map.put("publisher", firebaseUser.getUid());

        reference.push().setValue(map);
        addComment.setText("");
    }

    private void readComments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();

                for(DataSnapshot data: snapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    commentList.add(comment);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPost(final String postid, final LinearLayout growth_holder, final LinearLayout tag_holder,
                          final LinearLayout description_holder, final TextView growth, final TextView tag,
                          final TextView description, final ImageView post_image, final TextView publish_time,
                          final TextView publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(babyid).child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);

                if (post.getPostType().equals("growth")) {
                    growth_holder.setVisibility(View.VISIBLE);
                    growth.setText(post.getGrowth());
                } else {
                    growth_holder.setVisibility(View.GONE);
                }

                //to display tag
                if (post.getPostType().equals("milestone")) {
                    tag_holder.setVisibility(View.VISIBLE);
                    tag.setText(post.getTag());
                } else {
                    tag_holder.setVisibility(View.GONE);
                }

                //set post image
                if (post.getPostImages() == null) {
                    post_image.setVisibility(View.GONE);
                } else {
                    post_image.setVisibility(View.VISIBLE);
                    Glide.with(PostDetailActivity.this).load(post.getPostImages()).override(800, 800).fitCenter().into(post_image);
                }

                //set description
                if (post.getDescription() == null) {
                    description_holder.setVisibility(View.GONE);
                } else {
                    description_holder.setVisibility(View.VISIBLE);
                    description.setText(post.getDescription());
                }

                //change userid(in post) to username(in database)
                publisherInfo(post.getPublisher(), publisher);
                publish_time.setText(post.getTime());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void publisherInfo(final String userid, final TextView publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                publisher.setText("Publisher: " + user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}