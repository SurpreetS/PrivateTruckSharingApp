


/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/

package com.example.week9;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HamburgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HamburgerFragment extends Fragment {

    public HamburgerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HamburgerFragment newInstance() {
        HamburgerFragment fragment = new HamburgerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Code to handle arguments if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hamburger, container, false);

        // Find the TextViews in the layout
        TextView home, account, yourOrders;
        home = view.findViewById(R.id.textViewHome);
        account = view.findViewById(R.id.textviewAccount);
        yourOrders = view.findViewById(R.id.textViewYourOrders);

        // Set a click listener for the 'home' TextView
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for 'home' TextView

                // Remove the current fragment from the back stack
                requireActivity().getSupportFragmentManager().popBackStack();

                // Create and replace the current fragment with HomeFragment
                Fragment fragment = HomeFragment.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivityLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Set a click listener for the 'account' TextView
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for 'account' TextView
                // Add your code here
            }
        });

        // Set a click listener for the 'yourOrders' TextView
        yourOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for 'yourOrders' TextView

                // Remove the current fragment from the back stack
                requireActivity().getSupportFragmentManager().popBackStack();

                // Create and replace the current fragment with MyOrdersFragment
                Fragment fragment = MyOrdersFragment.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate(); // Remove the current fragment from the back stack (optional)
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.mainActivityLayout, fragment);
                transaction.addToBackStack("MyOrdersFragment"); // Add the transaction to the back stack with a unique name
                transaction.commit();
            }
        });

        return view;
    }
}