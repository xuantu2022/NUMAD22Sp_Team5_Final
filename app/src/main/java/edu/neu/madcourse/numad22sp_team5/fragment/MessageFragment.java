package edu.neu.madcourse.numad22sp_team5.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import edu.neu.madcourse.numad22sp_team5.ItemMessage;
import edu.neu.madcourse.numad22sp_team5.ItemMessageAdapter;
import edu.neu.madcourse.numad22sp_team5.MainActivity;
import edu.neu.madcourse.numad22sp_team5.R;


public class MessageFragment extends Fragment {
    private ArrayList<ItemMessage> messages = new ArrayList<>();

    private RecyclerView messageView;
    private ItemMessageAdapter messageAdapter;
    private RecyclerView.LayoutManager rLayoutManger;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        createMessageView(view);
        addMessage(0, "system");
        addMessage(0, "baby1 moment");
        return view;
    }

    private void createMessageView(View view) {
        messageView = view.findViewById(R.id.recycler_view);
        messageView.setHasFixedSize(true);
        rLayoutManger = new LinearLayoutManager(getContext());
        messageView.setLayoutManager(rLayoutManger);
        messageAdapter = new ItemMessageAdapter(messages);
        messageView.setAdapter(messageAdapter);
    }

    private void addMessage(int position, String name) {
        messages.add(position, new ItemMessage(name));
        messageAdapter.notifyItemInserted(position);
    }
}