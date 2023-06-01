package com.example.week9;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import sqllitehelper.DatabaseHelper;
import sqllitehelper.RecyclerviewDatabaseHelper;
import sqllitehelper.UserData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    boolean transactionInProgress = false;


    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        RecyclerviewDatabaseHelper databaseHelper;
        databaseHelper= new RecyclerviewDatabaseHelper(getContext());


        databaseHelper.insertTruck(new MyDataModel("Lion Truck", "2 ton truck for hire",R.drawable.truck1));
        databaseHelper.insertTruck(new MyDataModel("Eastern Plant Truck", "5 ton truck for hire",R.drawable.truck6));
        databaseHelper.insertTruck(new MyDataModel("Budget Truck", "2 ton truck for hire",R.drawable.truck2));
        databaseHelper.insertTruck(new MyDataModel("Abel Truck", "Construction truck for hire",R.drawable.truck5));
        databaseHelper.insertTruck(new MyDataModel("Sixt Truck", "2 ton truck for hire",R.drawable.truck3));
        databaseHelper.insertTruck(new MyDataModel("Budget Truck", "Pallet ton truck for hire",R.drawable.truck4));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        ImageView hamburgerIcon = view.findViewById(R.id.hamburgerImage);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton4);

        RecyclerView truckRecyclerView = view.findViewById(R.id.recyclerView4);
        RecyclerView.LayoutManager layoutManager;
        ArrayList<MyDataModel> newsListArray;
        MyAdapter myAdapter;


        newsListArray = new ArrayList<>();
        RecyclerviewDatabaseHelper databaseHelper;
        databaseHelper= new RecyclerviewDatabaseHelper(getContext());

        List<MyDataModel> retrievedData = databaseHelper.getDataFromDatabase();
        newsListArray.addAll(retrievedData);


        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);

        myAdapter = new MyAdapter(getActivity(), newsListArray);
        truckRecyclerView.setAdapter(myAdapter);
        truckRecyclerView.setLayoutManager(layoutManager);

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = HamburgerFragment.newInstance();


                if (!transactionInProgress) {
                    // Add the fragment
                    transaction.add(R.id.hamburgerLayout, fragment, "HamburgerFragment");
                    transaction.addToBackStack(null);
                    transactionInProgress = true;
                } else {
                    // Remove the fragment
                    Fragment existingFragment = fragmentManager.findFragmentByTag("HamburgerFragment");
                    if (existingFragment != null) {
                        transaction.remove(existingFragment);
                        transactionInProgress = false;
                    }
                }

                transaction.commit();

            }


        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = NewDeliveryFragment.newInstance();
                // Start new transaction to replace current fragment with SignupFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivityLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });



        return view;
    }
}