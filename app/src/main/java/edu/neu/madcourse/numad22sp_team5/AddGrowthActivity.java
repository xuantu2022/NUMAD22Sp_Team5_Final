package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

public class AddGrowthActivity extends AppCompatActivity {

    String babyid;
    EditText height;
    EditText weight;
    Button post;
    String growth;

    FirebaseDatabase db = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_growth);

//        Bundle extras = getIntent().getExtras();
//        if(extras != null){
//            babyid = extras.getString("babyid");
//            Log.i("babyid from extras: %s", babyid);
//        }

        SharedPreferences babyPref = getSharedPreferences("babyInfo", MODE_PRIVATE);
        babyid = babyPref.getString("babyid","");


        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        post = findViewById(R.id.growth_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(height.getText().toString().equals("") || weight.getText().toString().equals("")){
                    Toast.makeText( AddGrowthActivity.this,"Please fill height and weight",Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                    String postid = reference.push().getKey();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String time = dtf.format(now);

                    growth = "Height  " + height.getText().toString() + "cm   Weight  " + weight.getText().toString()+"kg";


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("babyid", babyid);
                    hashMap.put("description", "");
                    hashMap.put("growth", growth);
                    hashMap.put("postid", postid);
                    hashMap.put("postImages", "");
                    hashMap.put("publisher", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    hashMap.put("tag", "");
                    hashMap.put("time", time);
                    hashMap.put("postType", "growth");

                    HashMap<String, Object> postHash = new HashMap<>();
                    postHash.put(postid, hashMap);
                    reference.child(babyid).updateChildren(postHash);

                    // add notification branch in firebase
                    DatabaseReference reference_notify = FirebaseDatabase.getInstance().getReference("Notification");
                    String notificationId = reference_notify.push().getKey();
                    HashMap<String, Object> notifyMap = new HashMap<>();
                    notifyMap.put("postid", postid);
                    notifyMap.put("postPublisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    notifyMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    notifyMap.put("type", "post");
                    notifyMap.put("time", time);
                    notifyMap.put("description", "");
                    notifyMap.put("postImage", "");
                    HashMap<String, Object> notifyHash = new HashMap<>();
                    notifyHash.put(notificationId, notifyMap);
                    reference_notify.child(babyid).updateChildren(notifyHash);

                    finish();
                }

            }
        });

    }
}