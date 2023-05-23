
/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/


package com.example.week9;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import sqllitehelper.DatabaseHelper;
import sqllitehelper.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    DatabaseHelper databaseHelper; // Declaring a variable of type DatabaseHelper

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment(); // Creating a new instance of the MainFragment class
        Bundle args = new Bundle(); // Creating a new instance of the Bundle class
        fragment.setArguments(args); // Setting the arguments for the fragment
        return fragment; // Returning the created fragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Perform any operations with the arguments if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false); // Inflating the fragment layout
        Button lostFoundButton = view.findViewById(R.id.button2); // Finding the button with the id "button2"
        Button newAdvertButton = view.findViewById(R.id.button); // Finding the button with the id "button"
        Button showOnMap = view.findViewById(R.id.button5);

        newAdvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = NewAdvertFragment.newInstance(); // Creating a new instance of the NewAdvertFragment class
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction(); // Starting a new fragment transaction
                transaction.replace(R.id.mainActivityLayout, fragment); // Replacing the current fragment with the NewAdvertFragment
                transaction.addToBackStack(null); // Adding the transaction to the back stack
                transaction.commit(); // Committing the transaction
            }
        });

        lostFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = LostFoundFragment.newInstance(); // Creating a new instance of the LostFoundFragment class
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction(); // Starting a new fragment transaction
                transaction.replace(R.id.mainActivityLayout, fragment); // Replacing the current fragment with the LostFoundFragment
                transaction.addToBackStack(null); // Adding the transaction to the back stack
                transaction.commit(); // Committing the transaction
            }
        });

        showOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext()); //declaring databaseHelper for getting the stored items

                List<UserData> userDataList = databaseHelper.getAllLostFoundItems(); //retrieving items from database

                Fragment fragment = ShowOnMapFragment.newInstance(userDataList);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction(); // Starting a new fragment transaction
                transaction.replace(R.id.mainActivityLayout, fragment); // Replacing the current fragment with the LostFoundFragment
                transaction.addToBackStack(null); // Adding the transaction to the back stack
                transaction.commit(); // Committing the transaction

            }
        });


        return view; // Returning the inflated view
    }
}