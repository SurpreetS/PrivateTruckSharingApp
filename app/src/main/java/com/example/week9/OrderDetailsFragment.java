package com.example.week9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import sqllitehelper.MyOrderData;
import sqllitehelper.MyOrderDatabaseHelper;

public class OrderDetailsFragment extends Fragment {
    private TextView textViewLength;
    private TextView textViewWidth;
    private TextView textViewHeight;
    private TextView textViewGoodType;
    private TextView textViewWeight;
    private TextView textViewFromSender;
    private TextView textViewPickupTime;
    private TextView textViewReceiverUsername;
    private TextView textViewDropoffTime;
    private ImageView imageView;
    private Button getEstimateButton;
    int position;
    // Retrieve the order data from the database helper
    MyOrderData orderData;

    private MyOrderDatabaseHelper databaseHelper;

    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;

    public static OrderDetailsFragment newInstance() {
        return new OrderDetailsFragment();
    }

    public static OrderDetailsFragment newInstance(int position, ArrayList<MyOrderData> orderList) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("orderList", orderList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the database helper
        databaseHelper = new MyOrderDatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        // Find views in the fragment layout
        textViewLength = view.findViewById(R.id.textViewLength);
        textViewWidth = view.findViewById(R.id.textViewWidth);
        textViewHeight = view.findViewById(R.id.textViewHeight);
        textViewGoodType = view.findViewById(R.id.textViewGoodType);
        textViewWeight = view.findViewById(R.id.textViewWeight);
        textViewFromSender = view.findViewById(R.id.textViewFromSender);
        textViewPickupTime = view.findViewById(R.id.textViewPickupTime);
        textViewReceiverUsername = view.findViewById(R.id.textViewReceiverUsername);
        textViewDropoffTime = view.findViewById(R.id.textViewEstdeliverydate);
        imageView = view.findViewById(R.id.imageView2);
        getEstimateButton = view.findViewById(R.id.getEstimateButton);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get the position from the arguments
        position = getArguments().getInt("position");
        // Retrieve the order data from the database helper
        orderData = databaseHelper.getOrderData(position);
        // Bind the order data to the views in the fragment layout
        textViewLength.setText("Length: " + orderData.getLength());
        imageView.setImageResource(orderData.getImage());
        textViewWidth.setText("Width: " + orderData.getWidth());
        textViewHeight.setText("Height: " + orderData.getHeight());
        textViewGoodType.setText("Type: " + orderData.getGoodType());
        textViewWeight.setText("Weight: " + orderData.getWeight());
        textViewFromSender.setText("From Sender: Surpreet");
        textViewPickupTime.setText("Pick up date: " + orderData.getPickupDate());
        textViewReceiverUsername.setText("To receiver: " + orderData.getReceiverName());
        textViewDropoffTime.setText("Drop off date: " + orderData.getPickupDate() );

        getEstimateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Fragment fragment = GetEstimateFragment.newInstance(position);
                // Start new transaction to replace current fragment with SignupFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivityLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


}