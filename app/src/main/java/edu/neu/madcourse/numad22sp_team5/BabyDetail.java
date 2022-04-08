package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BabyDetail extends AppCompatActivity {
    private Button next;
    private EditText nickname, dob;
    private RadioGroup gender;
    private ImageView headshot;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_detail);

        next = findViewById(R.id.next);
        nickname = findViewById(R.id.nickname_input);
        dob = findViewById(R.id.dob_input);
        gender = findViewById(R.id.radio_gender);

        // click next button to go back to baby list activity
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BabyDetail.this, BabyListActivity.class));
            }
        });
    }


}