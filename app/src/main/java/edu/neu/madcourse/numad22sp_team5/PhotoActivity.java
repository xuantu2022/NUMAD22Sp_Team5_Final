package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.content.Intent;

import android.os.Bundle;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button add;

    ArrayList<Uri> uri = new ArrayList<>();
    RecyclerAdapter adapter;

    private static final int Read_Permission = 101;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        recyclerView = findViewById(R.id.recyclerView_Gallery_Images);
        add = findViewById(R.id.btn_add);

        adapter = new RecyclerAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(PhotoActivity.this,3));
        recyclerView.setAdapter(adapter);

        if(ContextCompat.checkSelfPermission(PhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(PhotoActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Read_Permission);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                }
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);


            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode==Activity.RESULT_OK){
            if(data.getClipData() != null){
                int x = data.getClipData().getItemCount();

                for(int i=0;i<x;i++){
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                adapter.notifyDataSetChanged();
            }else if(data.getData() != null){
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));

            }
        }

    }
}