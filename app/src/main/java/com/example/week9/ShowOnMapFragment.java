package com.example.week9; // Package declaration

import android.os.Bundle; // Importing required classes from the Android framework
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate; // Importing required classes from the Google Maps library
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import sqllitehelper.UserData; // Importing a custom class

public class ShowOnMapFragment extends Fragment implements OnMapReadyCallback { // Defining a class that extends Fragment and implements OnMapReadyCallback

    private static final String ARG_USER_DATA_LIST = "user_data_list"; // Declaration of a constant variable
    private List<UserData> userDataList; // Declaration of a List variable to hold user data for the fragment

    public static ShowOnMapFragment newInstance(List<UserData> userDataList) { // Static method to create a new instance of the fragment
        ShowOnMapFragment fragment = new ShowOnMapFragment(); // Creating a new instance of the fragment
        Bundle args = new Bundle(); // Creating a new Bundle object
        args.putParcelableArrayList(ARG_USER_DATA_LIST, new ArrayList<>(userDataList)); // Storing the user data list in the arguments bundle
        fragment.setArguments(args); // Setting the arguments for the fragment
        return fragment; // Returning the created fragment
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // Overriding the onCreate method of the Fragment class
        super.onCreate(savedInstanceState); // Calling the superclass implementation
        if (getArguments() != null) {
            // Retrieve the data passed to the fragment
            userDataList = getArguments().getParcelableArrayList(ARG_USER_DATA_LIST); // Retrieving the user data list from the arguments bundle
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) { // Overriding the onCreateView method of the Fragment class
        View view = inflater.inflate(R.layout.fragment_show_on_map, container, false); // Inflating the layout for the fragment

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map); // Finding the SupportMapFragment in the layout
        mapFragment.getMapAsync(this); // Setting the OnMapReadyCallback for the map fragment

        return view; // Returning the inflated view
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) { // Overriding the onMapReady method of the OnMapReadyCallback interface
        if (userDataList != null) { // Checking if the user data list is not null
            for (UserData userData : userDataList) { // Iterating over the user data list
                LatLng location = new LatLng(userData.getLocationLatLng().latitude, userData.getLocationLatLng().longitude); // Creating a LatLng object from user data location
                googleMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(userData.getDescription())); // Adding a marker to the Google Map for each user data
            }

            if (!userDataList.isEmpty()) { // Checking if the user data list is not empty
                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder(); // Creating a LatLngBounds.Builder object
                for (UserData userData : userDataList) { // Iterating over the user data list
                    boundsBuilder.include(userData.getLocationLatLng()); // Including each user data location in the bounds builder
                }

                LatLngBounds bounds = boundsBuilder.build(); // Building the LatLngBounds object
                int padding = 100; // Adjusting the padding as desired
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding); // Creating a CameraUpdate object to move the camera to the bounds
                googleMap.moveCamera(cameraUpdate); // Moving the camera to the specified bounds
            }
        }
    }

}
