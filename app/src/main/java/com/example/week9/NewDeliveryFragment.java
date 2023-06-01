package com.example.week9;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewDeliveryFragment extends Fragment {

    private String pickupLocation;
    private String dropoffLocation;
    Button nextButton;
    EditText editTextPickupTime;
    CalendarView calendarView;
    EditText editTextReceiverName;
    EditText editTextPickupLocation;
    EditText editTextDropoffLocation;
    private LatLng pickupLatLng;
    private LatLng dropoffLatLng;
    private boolean isPickupLocationSelected = false; // Flag to track the selected location

    public NewDeliveryFragment() {
        // Required empty public constructor
    }

    public static NewDeliveryFragment newInstance() {
        return new NewDeliveryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_delivery, container, false);

        nextButton = view.findViewById(R.id.nextbutton);
        editTextPickupTime = view.findViewById(R.id.editTextPickupTime);
        calendarView = view.findViewById(R.id.calendarView);
        editTextReceiverName = view.findViewById(R.id.editTextReceiverName);
        editTextPickupLocation = view.findViewById(R.id.editTextPickupLocation);
        editTextDropoffLocation = view.findViewById(R.id.editTextdropoffLocation);

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        }

        editTextPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickupLocationSelected = true; // Set the flag to indicate pickup location selection
                startLocationAutocomplete(v);
            }
        });

        editTextDropoffLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPickupLocationSelected = false; // Set the flag to indicate drop-off location selection
                startLocationAutocomplete(v);
            }
        });

        nextButton.setOnClickListener(v -> {
            String pickupTime = editTextPickupTime.getText().toString();
            String pickupDate = getSelectedDateString();
            String receiverName = editTextReceiverName.getText().toString();
            String pickupLocation = editTextPickupLocation.getText().toString();

            Fragment fragment = NewDeliveryNextFragment.newInstance(pickupLocation, pickupDate, pickupTime, receiverName, pickupLatLng, dropoffLatLng,dropoffLocation);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.mainActivityLayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Update the calendar instance to the selected date
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);

                // Set the selected date to the calendarView
                calendarView.setDate(selectedCalendar.getTimeInMillis(), true, true);
            }
        });

        return view;
    }

    private void startLocationAutocomplete(View v) {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(requireContext());
        startAutocomplete.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        String locationName = place.getName();
                        LatLng locationLatLng = place.getLatLng();

                        if (locationLatLng != null && locationName != null) {
                            if (isPickupLocationSelected) { // Check the flag to update the correct EditText view
                                pickupLocation = locationName;
                                editTextPickupLocation.setText(locationName);
                                pickupLatLng = locationLatLng;
                            } else {
                                dropoffLocation = locationName;
                                editTextDropoffLocation.setText(locationName);
                                dropoffLatLng = locationLatLng;

                            }
                        }
                    }
                } else if (result.getResultCode() == getActivity().RESULT_CANCELED) {
                    Toast.makeText(requireContext(), "User canceled autocomplete", Toast.LENGTH_SHORT).show();
                }
            });

    private String getSelectedDateString() {
        long selectedDateInMillis = calendarView.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDateInMillis);

        // Format the date as per your desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date selectedDate = calendar.getTime();
        return dateFormat.format(selectedDate);
    }
}
