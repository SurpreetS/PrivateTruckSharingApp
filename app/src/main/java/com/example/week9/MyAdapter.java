/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/



package com.example.week9;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import sqllitehelper.MyOrderData;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    // Define variables for the context and list of data models
    Context mContext;
    ArrayList<MyDataModel> trucksList;

    // Constructor for MyAdapter class
    public MyAdapter(Context mContext, ArrayList<MyDataModel> trucksList) {
        // Initialize context and list of data models
        this.mContext = mContext;
        this.trucksList = trucksList;
    }


    // Override onCreateViewHolder method to inflate layout for each item in RecyclerView
    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate layout for each item in RecyclerView
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_layout, parent, false);
        // Return ViewHolder with inflated view
        return new MyViewHolder(view);
    }

    // Override onBindViewHolder method to bind data to layout views
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        // Bind data to views in layout
        holder.mTextView.setText(trucksList.get(position).getTruckName());
        holder.mTextViewDescription.setText(trucksList.get(position).getDescription());
        holder.imageView.setImageResource(trucksList.get(position).getImage());
    }

    // Override getItemCount method to return the size of the list of data models
    @Override
    public int getItemCount() {
        return trucksList.size();
    }

    // Define MyViewHolder class to hold layout views for each item in RecyclerView
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Define layout views
        TextView mTextView, mTextViewDescription;
        ImageView imageView, shareImageView;

        // Constructor for MyViewHolder class
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views by their IDs in layout
            mTextView = itemView.findViewById(R.id.nameTextView);
            mTextViewDescription = itemView.findViewById(R.id.descriptionTextView);
            imageView = itemView.findViewById(R.id.imageView);
            // Set click listener for item view
            itemView.setOnClickListener(this);
            shareImageView = itemView.findViewById(R.id.imageView3);
            // Set click listener for item view
            itemView.setOnClickListener(this);
            shareImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MyDataModel truck = trucksList.get(position);
                        // Perform the share action for the clicked truck
                        performShareAction(truck);
                    }

                }
            });
        }

        private void performShareAction(MyDataModel order) {
            String shareTitle = order.getTruckName();
            String shareDescription = order.getDescription();
            // You can add more details from the order object as needed

            String shareText = shareTitle + "\n" + shareDescription;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            Intent chooserIntent = Intent.createChooser(shareIntent, "Share via");

            if (shareIntent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(chooserIntent);
            } else {
                Toast.makeText(mContext, "No app available to share", Toast.LENGTH_SHORT).show();
            }
        }

        // Override onClick method to handle item click events
        @Override
        public void onClick(View v) {
//            // Create new NewsFragment with position and list of data models as arguments
//            Fragment fragment = OrderDetailsFragment.newInstance();
//            // Get FragmentManager from current activity
//            FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
//            // Start new transaction to replace current fragment with NewsFragment
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.replace(R.id.mainActivityLayout, fragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
        }
    }
}