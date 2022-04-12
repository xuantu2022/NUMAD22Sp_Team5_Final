package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class AlbumFullImageActivity extends AppCompatActivity {

    ImageView image;
    ImageView postDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_full_image);

        // get current babyid, postid, postImages, publisher
        Intent intent = getIntent();

        String babyid = intent.getStringExtra("babyid");
        String postid = intent.getStringExtra("postid");
        String imageUrl = intent.getStringExtra("postImages");
        String publisher = intent.getStringExtra("publisher");

        String path = "Posts/" + babyid + "/" + postid;

        image = findViewById(R.id.imageView_album_full);
        postDetail = findViewById(R.id.imageView_post_detail);
        postDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), PostDetailActivity.class);
                intent1.putExtra("babyid", babyid);
                intent1.putExtra("postid", postid);
                intent1.putExtra("publisherid", publisher);
                startActivity(intent1);
            }
        });

        Glide.with(getApplicationContext()).load(imageUrl).into(image);
    }
}