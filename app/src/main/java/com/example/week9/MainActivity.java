/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/


package com.example.week9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create a new instance of MainFragment
        Fragment fragment = MainFragment.newInstance(); // Creating a new instance of the MainFragment class and assigning it to the fragment variable
        // Begin a transaction to replace the content of the activity with the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction(); // Getting the FragmentManager and starting a new FragmentTransaction
        transaction.replace(R.id.mainActivityLayout, fragment, "Main Fragment"); // Replacing the content of the mainActivityLayout container with the fragment and providing a tag
        transaction.commit(); // Commit the transaction and replace the content

    }

}