
/*
    Name        :  Surpreet Singh
    Student ID  :  218663803
    Unit No.    :  SIT305
*/


package com.example.week9;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sqllitehelper.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {


    DatabaseHelper databaseHelper;
    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Button signupButton = view.findViewById(R.id.button2);
        Button loginButton = view.findViewById(R.id.button);
        EditText userName = view.findViewById(R.id.editTextUserName);
        EditText passWord = view.findViewById(R.id.editTextPassword);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userName.getText().toString();
                String password = passWord.getText().toString();

                databaseHelper= new DatabaseHelper(getContext());

                boolean result = databaseHelper.getUser(
                        userName.getText().toString(), passWord.getText().toString()
                );

                if(result ==true){
                    Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                    Fragment fragment = HomeFragment.newInstance();
                    // Start new transaction to replace current fragment with SignupFragment
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainActivityLayout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Toast.makeText(getContext(), "User Does not Exist", Toast.LENGTH_SHORT).show();

                }

            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = SignUpFragment.newInstance();
                // Start new transaction to replace current fragment with SignupFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainActivityLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }
}