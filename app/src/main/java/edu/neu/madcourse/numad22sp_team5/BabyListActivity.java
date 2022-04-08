package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This class is a test class to test back button on action bar.
 */

public class BabyListActivity extends AppCompatActivity implements ItemClickListener{
    private RecyclerView mView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_list);
        initView();

        mButton = findViewById(R.id.floatingActionButton);
    }

    private void initView() { //初始化
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mView = (RecyclerView) findViewById(R.id.recycler_view);
        mView.setHasFixedSize(true);
        mView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(this);
        mView.setAdapter(mAdapter);
    }


    public void onClickFAB(View view) {
        openBabyDetail();
    }


    public void openBabyDetail() {
        Intent intent = new Intent(this, BabyDetail.class);
        startActivity(intent);
    }


    @Override
    public void onItemClick(int position) {

    }
}