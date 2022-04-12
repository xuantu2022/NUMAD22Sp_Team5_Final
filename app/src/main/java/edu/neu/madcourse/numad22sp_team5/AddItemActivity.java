package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class AddItemActivity extends AppCompatActivity {
    Button photo;
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
    }

    public void openPhotoAct(){
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("babyid",babyid);
        startActivity(intent);


    }
}