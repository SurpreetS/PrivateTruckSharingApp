package com.example.week9;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import sqllitehelper.DatabaseHelper;
import sqllitehelper.UserData;

public class NewAdvertFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private EditText locationEditText;
    private LatLng locationLatLng;
    Button getCurrentLocationButton;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    public NewAdvertFragment() {
        // Required empty public constructor
    }

    public static NewAdvertFragment newInstance() {
        return new NewAdvertFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Retrieve any arguments passed to the fragment
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_advert, container, false);

        // Initialize UI elements
        RadioButton lostRadioButton = view.findViewById(R.id.radioButtonLost);
        RadioButton foundRadioButton = view.findViewById(R.id.radioButtonFound);
        EditText nameEditText = view.findViewById(R.id.editTextName);
        EditText phoneEditText = view.findViewById(R.id.editTextPhoneNumber);
        EditText descriptionEditText = view.findViewById(R.id.editTextDescription);
        EditText dateEditText = view.findViewById(R.id.editTextDate);
        locationEditText = view.findViewById(R.id.editTextLocation);
        Button saveButton = view.findViewById(R.id.button3);
        getCurrentLocationButton = view.findViewById(R.id.buttonGetCurrentLocation);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }

        // Set click listener for locationEditText to open the autocomplete activity
        locationEditText.setOnClickListener(v -> {
            // Specify the fields to be returned by the autocomplete activity
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(requireContext());
            startAutocomplete.launch(intent);
        });

        // Set click listener for getCurrentLocationButton to retrieve the current location
        getCurrentLocationButton.setOnClickListener(v -> {
            // Check for location permissions
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request location permissions if not granted
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            // Get the last known location
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    // Retrieve the latitude and longitude
                    locationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Geocode the location to get the place name
                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!addresses.isEmpty()) {
                            Address address = addresses.get(0);
                            String locationName = address.getAddressLine(0);  // Get the location name
                            locationEditText.setText(locationName);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(requireContext(), "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Set click listener for saveButton to save the advert data
        saveButton.setOnClickListener(v -> {
            // Retrieve data from EditText fields
            String name = nameEditText.getText().toString();
            String phoneNumber = phoneEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String location = locationEditText.getText().toString();

            // Insert data into the database
            databaseHelper = new DatabaseHelper(getActivity());
            UserData myOrderData = new UserData(name, phoneNumber, description, date, location, locationLatLng);
            long result = databaseHelper.insertData(myOrderData);

            // Check if the insertion was successful
            if (result != -1) {
                Toast.makeText(getActivity(), "Item created successfully", Toast.LENGTH_SHORT).show();
                // Navigate to the LostFoundFragment
                Fragment fragment = LostFoundFragment.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.mainActivityLayout, fragment);
                transaction.addToBackStack("LostFoundFragment");
                transaction.commit();
            } else {
                Toast.makeText(getActivity(), "Failed to create Item", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Activity result launcher for the autocomplete activity
    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        // Retrieve the selected place from the autocomplete activity
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        locationEditText.setText(place.getName());
                        locationLatLng = place.getLatLng();
                    }
                } else if (result.getResultCode() == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "User canceled autocomplete", Toast.LENGTH_SHORT).show();
                }
            });

    // Handle the result of location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, simulate a click on getCurrentLocationButton
                getCurrentLocationButton.performClick();
            } else {
                Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
