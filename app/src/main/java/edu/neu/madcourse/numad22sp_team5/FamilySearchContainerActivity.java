package edu.neu.madcourse.numad22sp_team5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.navigation.NavigationBarView;

import edu.neu.madcourse.numad22sp_team5.fragment.SearchFragment;

public class FamilySearchContainerActivity extends AppCompatActivity {

    Fragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_search_container);

        searchFragment = new SearchFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.search_layout_container, searchFragment);
        fragmentTransaction.commit();
    }
}