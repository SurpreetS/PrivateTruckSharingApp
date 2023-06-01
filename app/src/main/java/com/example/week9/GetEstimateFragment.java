

/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/


package com.example.week9;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import sqllitehelper.MyOrderData;
import sqllitehelper.MyOrderDatabaseHelper;

import com.google.android.gms.maps.model.PolylineOptions;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.activity.result.ActivityResultLauncher;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import java.util.List;


public class GetEstimateFragment extends Fragment implements OnMapReadyCallback {

    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;

    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;

    private PaymentsClient paymentsClient;
    private ActivityResultLauncher<Intent> paymentResultLauncher;
    private MyOrderDatabaseHelper databaseHelper;
    private GoogleMap googleMap;
    private Marker pickupMarker;
    private Marker dropoffMarker;
    private TextView approxFareTextview;
    private TextView approxTravelTimeTextview;
    private MyOrderData orderData;
    private Polyline routePolyline;

    public GetEstimateFragment() {
        // Required empty public constructor
    }

    public static com.example.week9.GetEstimateFragment newInstance() {
        return new com.example.week9.GetEstimateFragment();
    }

    public static com.example.week9.GetEstimateFragment newInstance(int position) {
        com.example.week9.GetEstimateFragment fragment = new com.example.week9.GetEstimateFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the database helper
        databaseHelper = new MyOrderDatabaseHelper(requireContext());
        paymentsClient = createPaymentsClient();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_estimate, container, false);

        Button callDriverButton = view.findViewById(R.id.buttonCallDriver);
        Button bookNowButton = view.findViewById(R.id.buttonBookNow);
        TextView pickupLocationTextview = view.findViewById(R.id.textViewPickupLocation);
        TextView dropOffLocationTextview = view.findViewById(R.id.textViewDropOffLocation);
        approxFareTextview = view.findViewById(R.id.textViewApproxFare);
        approxTravelTimeTextview = view.findViewById(R.id.textViewApproxTravelTime);



        int position = getArguments().getInt("position");
        // Retrieve the order data from the database helper
        orderData = databaseHelper.getOrderData(position);
        pickupLocationTextview.setText("Pickup location: " + orderData.getPickupLocation());
        dropOffLocationTextview.setText("Drop-off location: " + orderData.getDropoffLocation());

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentAppsDialog();

            }
        });

        callDriverButton.setOnClickListener(v -> {
            String phoneNumber = "0470417267"; // Replace with the actual phone number

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                startPhoneActivity(phoneNumber);
            } else {
                requestCallPhonePermission();
            }
        });

        return view;
    }


    private PaymentsClient createPaymentsClient() {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build();
        return Wallet.getPaymentsClient(requireActivity(), walletOptions);
    }

    private void showPaymentAppsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select a payment app");

        final Map<String, String> paymentApps = new HashMap<>();
        paymentApps.put("PayPal", "https://www.paypal.com/");
        paymentApps.put("Venmo", "https://www.venmo.com/");
        paymentApps.put("Google Pay", "https://pay.google.com/");
        paymentApps.put("Apple Pay", "https://www.apple.com/apple-pay/");

        final String[] paymentAppNames = paymentApps.keySet().toArray(new String[0]);

        builder.setItems(paymentAppNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedPaymentAppName = paymentAppNames[which];
                String selectedPaymentAppUrl = paymentApps.get(selectedPaymentAppName);
                processPayment(selectedPaymentAppUrl);
            }
        });

        builder.show();
    }

    private void processPayment(String paymentAppUrl) {
        PaymentDataRequest request = createPaymentDataRequest(paymentAppUrl);
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    requireActivity(),
                    LOAD_PAYMENT_DATA_REQUEST_CODE
            );
        } else {
            Toast.makeText(requireContext(), "Failed to create payment request.", Toast.LENGTH_SHORT).show();
        }
    }

    private PaymentDataRequest createPaymentDataRequest(String paymentAppUrl) {
        Uri uri = Uri.parse(paymentAppUrl);
        String paymentGateway = uri.getHost();
        if ("pay.google.com".equals(paymentGateway)) {
            return createGooglePayPaymentDataRequest();
        }

        return null;
    }

    private PaymentDataRequest createGooglePayPaymentDataRequest() {
        // Customize the payment request as per your requirements
        return PaymentDataRequest.fromJson("{\n" +
                "  \"apiVersion\": 2,\n" +
                "  \"apiVersionMinor\": 0,\n" +
                "  \"allowedPaymentMethods\": [\n" +
                "    {\n" +
                "      \"type\": \"CARD\",\n" +
                "      \"parameters\": {\n" +
                "        \"allowedAuthMethods\": [\"PAN_ONLY\", \"CRYPTOGRAM_3DS\"],\n" +
                "        \"allowedCardNetworks\": [\"AMEX\", \"DISCOVER\", \"MASTERCARD\", \"VISA\"]\n" +
                "      },\n" +
                "      \"tokenizationSpecification\": {\n" +
                "        \"type\": \"PAYMENT_GATEWAY\",\n" +
                "        \"parameters\": {\n" +
                "          \"gateway\": \"example\",\n" +
                "          \"gatewayMerchantId\": \"your_gateway_merchant_id\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"transactionInfo\": {\n" +
                "    \"totalPriceStatus\": \"FINAL\",\n" +
                "    \"totalPrice\": \"10.00\",\n" +
                "    \"currencyCode\": \"AUD\"\n" +
                "  },\n" +
                "  \"merchantInfo\": {\n" +
                "    \"merchantName\": \"Your Merchant Name\"\n" +
                "  }\n" +
                "}");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    if (paymentData != null) {
                        // Handle the payment success and show the toast message
                        Toast.makeText(requireContext(), "Payment successful.", Toast.LENGTH_SHORT).show();

                        Log.v("Success","S");

                    } else {
                        Toast.makeText(requireContext(), "Payment data is null.", Toast.LENGTH_SHORT).show();
                        Log.v("Null","N");
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(requireContext(), "Payment canceled.", Toast.LENGTH_SHORT).show();
                    Log.v("Payment canceled","P");
                    break;
                case AutoResolveHelper.RESULT_ERROR:
                    Toast.makeText(requireContext(), "Payment error.", Toast.LENGTH_SHORT).show();
                    Log.v("Payment Error","P-E");
                    break;
            }
        }
    }

    private void requestCallPhonePermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{Manifest.permission.CALL_PHONE},
                CALL_PHONE_PERMISSION_REQUEST_CODE
        );
    }

    private void startPhoneActivity(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                String phoneNumber = "0470417267";
                startPhoneActivity(phoneNumber);
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Call Phone permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Display pickup and drop-off markers on the map
        displayPickupMarker();
        displayDropoffMarker();

        calculateDistanceAndTime();

        // Display the route path
        displayRoute();
    }

    private void displayRoute() {
        LatLng pickupLatLng = orderData.getPickupLatLng();
        LatLng dropoffLatLng = orderData.getDropoffLatLng();

        if (pickupLatLng != null && dropoffLatLng != null) {
            GeoApiContext geoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyCC_ZWKwMo7wapVeqdzmy4iBlMjkQDfF_c")
                    .build();

            DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .origin(new com.google.maps.model.LatLng(pickupLatLng.latitude, pickupLatLng.longitude))
                    .destination(new com.google.maps.model.LatLng(dropoffLatLng.latitude, dropoffLatLng.longitude));

            try {
                DirectionsResult result = request.await();

                if (result.routes != null && result.routes.length > 0) {
                    List<LatLng> pathPoints = new ArrayList<>();

                    // Add the pickup location to the path
                    pathPoints.add(pickupLatLng);

                    // Add each waypoint to the path
                    for (com.google.maps.model.LatLng waypoint : result.routes[0].overviewPolyline.decodePath()) {
                        pathPoints.add(new LatLng(waypoint.lat, waypoint.lng));
                    }

                    // Add the drop-off location to the path
                    pathPoints.add(dropoffLatLng);

                    // Create the polyline options
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(pathPoints)
                            .color(ContextCompat.getColor(requireContext(), R.color.purple_500))
                            .width(getResources().getDimensionPixelSize(R.dimen.width));

                    routePolyline = googleMap.addPolyline(polylineOptions);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





    private void displayPickupMarker() {
        LatLng pickupLatLng = orderData.getPickupLatLng();

        if (pickupLatLng != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(pickupLatLng)
                    .title("Pickup Location");

            pickupMarker = googleMap.addMarker(markerOptions);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickupLatLng, 12));
        }
    }





    private void displayDropoffMarker() {
        LatLng dropoffLatLng = orderData.getDropoffLatLng();

        if (dropoffLatLng != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(dropoffLatLng)
                    .title("Drop-off Location");

            dropoffMarker = googleMap.addMarker(markerOptions);
        }
    }


    private void calculateDistanceAndTime() {
        LatLng pickupLatLng = orderData.getPickupLatLng();
        LatLng dropoffLatLng = orderData.getDropoffLatLng();

        if (pickupLatLng != null && dropoffLatLng != null) {
            GeoApiContext geoApiContext = new GeoApiContext.Builder()
                    .apiKey("AIzaSyDg8kI3BDG1RJI6hUCEOGwD8SgJ3338-M4")
                    .build();

            DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext)
                    .mode(TravelMode.DRIVING)
                    .origin(new com.google.maps.model.LatLng(pickupLatLng.latitude, pickupLatLng.longitude))
                    .destination(new com.google.maps.model.LatLng(dropoffLatLng.latitude, dropoffLatLng.longitude));

            try {
                DirectionsResult result = request.await();

                if (result.routes != null && result.routes.length > 0) {
                    long distanceInMeters = result.routes[0].legs[0].distance.inMeters;
                    long durationInSeconds = result.routes[0].legs[0].duration.inSeconds;

                    // Convert distance and duration to readable format (e.g., kilometers and minutes)
                    double distanceInKilometers = distanceInMeters / 1000.0;
                    long durationInMinutes = durationInSeconds / 60;
                    // Calculate fare
                    double fare = calculateFare(distanceInKilometers, durationInMinutes);

                    // Store the distance and time in variables
                    String distance = String.format("%.2f km", distanceInKilometers);
                    String duration = String.format("%d mins", durationInMinutes);
                    String fareString = String.format("%.2f", fare);

                    // Update the appropriate TextViews
                    approxFareTextview.setText("Fare: $" + fareString);
                    approxTravelTimeTextview.setText("Travel Time: " + duration);

                    // Toast the message
                    String toastMessage = "Distance: " + distance + ", Travel Time: " + duration + ", Fare: $" + fareString;
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private double calculateFare(double distanceInKilometers, long durationInMinutes) {
        double ratePerKilometer = 1.5;  // Adjust this rate as needed
        double ratePerMinute = 0.5;  // Adjust this rate as needed

        double fare = (distanceInKilometers * ratePerKilometer) + (durationInMinutes * ratePerMinute);
        return fare;
    }




}