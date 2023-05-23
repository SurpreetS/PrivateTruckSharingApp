/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/


package com.example.week9;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sqllitehelper.DatabaseHelper;
import sqllitehelper.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LostFoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LostFoundFragment extends Fragment {

    // Empty constructor for the fragment
    public LostFoundFragment() {
        // Required empty public constructor
    }

    // Factory method to create an instance of the fragment
    public static LostFoundFragment newInstance() {
        LostFoundFragment fragment = new LostFoundFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Add any initialization or setup logic here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost_found, container, false);

        // Create an instance of the DatabaseHelper
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

        // Find the RecyclerView in the layout
        RecyclerView lostFoundRecyclerView = view.findViewById(R.id.recyclerView);

        // Declare variables for the RecyclerView
        RecyclerView.LayoutManager layoutManager;
        ArrayList<UserData> newsListArray = new ArrayList<>();
        MyAdapter myAdapter;

        // Retrieve all lost and found items from the database
        List<UserData> retrievedData = databaseHelper.getAllLostFoundItems();

        // Add the retrieved items to the newsListArray
        newsListArray.addAll(retrievedData);

        // Create a LinearLayoutManager for the RecyclerView
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        // Create an instance of the custom adapter with the activity and newsListArray
        myAdapter = new MyAdapter(getActivity(), newsListArray);

        // Set the adapter and layout manager for the RecyclerView
        lostFoundRecyclerView.setAdapter(myAdapter);
        lostFoundRecyclerView.setLayoutManager(layoutManager);

        // Return the inflated view
        return view;
    }
}
