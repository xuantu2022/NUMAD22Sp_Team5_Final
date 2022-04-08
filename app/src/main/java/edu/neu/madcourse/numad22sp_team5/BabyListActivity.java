package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.neu.madcourse.numad22sp_team5.Adapter.BabyAdapter;


public class BabyListActivity extends AppCompatActivity implements ItemClickListener{
    private RecyclerView mView;
    private BabyAdapter mAdapter;
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
        mAdapter = new BabyAdapter(this);
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