package edu.neu.madcourse.numad22sp_team5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationBarView;

import edu.neu.madcourse.numad22sp_team5.fragment.HomeFragment;
import edu.neu.madcourse.numad22sp_team5.fragment.MessageFragment;
import edu.neu.madcourse.numad22sp_team5.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    //BottomNavigationView bottomNavigationView;
    NavigationBarView navigationBarView;
    Fragment selectedFragment = null;

    //add notification indicator
    View notificationIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationBarView = (NavigationBarView) findViewById(R.id.bottom_navigation);
        /* disable customized toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/

        //enable back button on action bar and hide title
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //bottom navigation bar selector
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_message:
                        selectedFragment = new MessageFragment();
                        //add notification indicator
                        if(notificationIndicator != null){
                            if(notificationIndicator.getVisibility() == View.VISIBLE){
                                notificationIndicator.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case R.id.nav_setting:
                        selectedFragment = new SettingFragment();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                }

                return true;
            }
        });


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        //add notification indicator
        showNotificationIndicator();
    }

    //add notification indicator
    private void showNotificationIndicator(){
        NavigationBarView itemView = (NavigationBarView) findViewById(R.id.bottom_navigation);
        notificationIndicator = LayoutInflater.from(this).inflate(R.layout.layout_indicator,navigationBarView,false);
        notificationIndicator.setVisibility(View.VISIBLE);
        itemView.addView(notificationIndicator);

    }

    /* disable camera on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.drawable.ic_back:
                Intent intent1 = new Intent(this,BabyListActivity.class);
                startActivity(intent1);
                return true;
            case R.id.add_items:
                Intent intent2 = new Intent(this,AddActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
}

