/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/
package com.example.week9;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import sqllitehelper.DatabaseHelper;
import sqllitehelper.UserData;

public class DetailsFragment extends Fragment {
    private static final String ARG_USER_DATA = "user_data";
    private UserData userData; // Holds the user data for the fragment
    private DatabaseHelper databaseHelper; // Helper class for database operations

    // Factory method to create a new instance of the fragment
    public static DetailsFragment newInstance(UserData userData) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_DATA, userData); // Store the user data in the arguments bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve the data passed to the fragment
            userData = getArguments().getParcelable(ARG_USER_DATA);
        }
        databaseHelper = new DatabaseHelper(requireContext()); // Create an instance of the database helper
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Find and initialize views in the layout
        TextView textViewDetails = view.findViewById(R.id.textViewDetails);
        Button removeButton = view.findViewById(R.id.button4);

        // Display the data in the views
        if (userData != null) {
            String details = "" + userData.getDescription() +
                    "\n\nOn Date: " + userData.getDate() +
                    "\n\nAt: " + userData.getLocation();

            textViewDetails.setText(details); // Set the details text to the text view
        }

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userData != null) {
                    // Remove the UserData from the database
                    boolean isDeleted = databaseHelper.deleteUserData(userData.getDescription());

                    if (isDeleted) {
                        Toast.makeText(getActivity(), "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Fragment fragment = LostFoundFragment.newInstance();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.mainActivityLayout, fragment);
                        transaction.addToBackStack(null);
                        getParentFragmentManager().popBackStackImmediate();
                        transaction.commit();
                    } else {
                        Toast.makeText(getActivity(), "Failed to delete Item", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view; // Return the inflated view for the fragment
    }
}
