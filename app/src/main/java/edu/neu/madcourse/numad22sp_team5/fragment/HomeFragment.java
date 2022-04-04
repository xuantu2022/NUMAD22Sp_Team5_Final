package edu.neu.madcourse.numad22sp_team5.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.neu.madcourse.numad22sp_team5.R;


public class HomeFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setImageResource(R.drawable.ic_add_items);


        // Inflate the layout for this fragment
        return view;
    }




}