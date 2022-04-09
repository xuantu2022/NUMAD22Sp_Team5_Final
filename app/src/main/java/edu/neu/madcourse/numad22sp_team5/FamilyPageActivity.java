package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class FamilyPageActivity extends AppCompatActivity {

    TextView family_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_page);

        family_add = findViewById(R.id.textView_family_add);
        family_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FamilySearchContainerActivity.class);
                startActivity(intent);
            }
        });
    }
}