package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class AlbumFullImageActivity extends AppCompatActivity {

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_full_image);

        image = findViewById(R.id.imageView_album_full);

        // get current babyid, postid, postImages
        Intent intent = getIntent();

        String babyid = intent.getStringExtra("babyid");
        String postid = intent.getStringExtra("postid");
        String imageUrl = intent.getStringExtra("postImages");

        String path = "Posts/" + babyid + "/" + postid;

        Glide.with(getApplicationContext()).load(imageUrl).into(image);
    }
}