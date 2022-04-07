package edu.neu.madcourse.numad22sp_team5.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.content.Intent;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.neu.madcourse.numad22sp_team5.AddItemActivity;
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddItemActivity();

            }
        });

        //add background pic for home fragment
        StorageReference backgroundRef = FirebaseStorage.getInstance().getReference().child("app_materials/bg1.jpg");
        StorageReference headshotRef = FirebaseStorage.getInstance().getReference().child("app_materials/baby1.jpeg");
        ImageView background = view.findViewById(R.id.background);
        ImageView headshot = view.findViewById(R.id.headshot);
        //center crop crop picture based on imageview size
        Glide.with(this).load(backgroundRef).centerCrop().into(background);
        Glide.with(this).load(headshotRef).centerCrop().into(headshot);



        // Inflate the layout for this fragment
        return view;
    }

    public void openAddItemActivity(){
        Intent intent = new Intent(getActivity(),AddItemActivity.class);
        startActivity(intent);
    }




}