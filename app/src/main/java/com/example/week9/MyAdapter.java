/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/



package com.example.week9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import sqllitehelper.UserData;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    // Context object to be used in the adapter
    Context mContext;

    // ArrayList to hold the data for the adapter
    ArrayList<UserData> dataList;

    public MyAdapter(Context mContext, ArrayList<UserData> dataList) {
        // Constructor to initialize the adapter with the context and data list
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view by inflating the layout file for each item in the RecyclerView
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycyclerview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind the data to the views in the ViewHolder
        holder.mTextViewDescription.setText(displayString(dataList.get(position).getDescription()));
    }

    @Override
    public int getItemCount() {
        // Return the size of the data list
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // ViewHolder class to hold the views for each item in the RecyclerView
        TextView mTextViewDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextView from the layout file
            mTextViewDescription = itemView.findViewById(R.id.itemTextView);
            // Set click listener on the itemView
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // Get the clicked data from the data list
                UserData clickedData = dataList.get(position);
                // Create a new instance of the DetailsFragment with the clicked data
                Fragment fragment = DetailsFragment.newInstance(clickedData);
                // Get the FragmentManager from the activity
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                // Start a new FragmentTransaction
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // Replace the current fragment with the DetailsFragment
                transaction.replace(R.id.mainActivityLayout, fragment);
                // Add the transaction to the back stack
                transaction.addToBackStack(null);
                // Commit the transaction
                transaction.commit();
            }
        }
    }

    public String displayString(String originalString) {
        // Split the original string into words
        String[] words = originalString.split("\\s+");
        // Create a StringBuilder to build the display text
        StringBuilder displayText = new StringBuilder();
        // Determine the number of words to display (up to 3)
        int count = Math.min(words.length, 3);
        // Append the first count words to the display text
        for (int i = 0; i < count; i++) {
            displayText.append(words[i]).append(" ");
        }
        // If there are more than 3 words, append "..." to the display text
        if (words.length > 3) {
            displayText.append("...");
        }
        // Trim the display text and convert it to a string
        String displayedText = displayText.toString().trim();
        // Return the displayed text
        return displayedText;
    }
}