package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageButton;

public class AddItemActivity extends AppCompatActivity {
    ImageButton photo;
    ImageButton growth;
    ImageButton milestone;
    String babyid;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            babyid = extras.getString("babyid");
            Log.i("babyid from extras: %s", babyid);
        }
        photo = findViewById(R.id.btn_photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openPhotoAct();


            }
        });

        growth = findViewById(R.id.btn_growth);
        growth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGrowthAct();
            }
        });

        milestone = findViewById(R.id.btn_milestone);
        milestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotoAct();

            }
        });


    }

    public void openPhotoAct(){
        Intent intent = new Intent(this, PhotoActivity.class);
//        intent.putExtra("babyid",babyid);
        startActivity(intent);
        //finish this activity
        finish();


    }

    public void openGrowthAct(){
        Intent i = new Intent(this,AddGrowthActivity.class);
//        i.putExtra("babyid",babyid);
        startActivity(i);
        finish();
    }
}