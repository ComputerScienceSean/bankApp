package com.example.bankapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.entities.BankAccount;
import com.example.bankapp.R;
import com.example.bankapp.entities.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterUser extends AppCompatActivity {

    private EditText cpr, firstname, lastname, phonenumber, email, address, password;
    private FirebaseDatabase database;
    public static final String TAG = "REGISTERUSERTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        init();
    }

    public void init() {
        this.cpr = findViewById(R.id.cpr);
        this.firstname = findViewById(R.id.firstname);
        this.lastname = findViewById(R.id.lastname);
        this.phonenumber = findViewById(R.id.phonenumber);
        this.email = findViewById(R.id.email);
        this.address = findViewById(R.id.address);
        this.password = findViewById(R.id.password);
        this.database = FirebaseDatabase.getInstance();
    }

    public void register(View view) {
        if (address.getText().toString().length() != 0 && email.getText().toString().length() != 0 && firstname.getText().toString().length() != 0 && lastname.getText().toString().length() != 0 && phonenumber.getText().toString().length() != 0) {
            if (password.getText().toString().length() >= 6) {
                if (cpr.getText().toString().length() == 10) {

                    final DatabaseReference dbRef = database.getReference("users/" + cpr.getText().toString());
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                final User user = new User(
                                        firstname.getText().toString(),
                                        lastname.getText().toString(),
                                        cpr.getText().toString(),
                                        phonenumber.getText().toString(),
                                        email.getText().toString(),
                                        password.getText().toString(),
                                        address.getText().toString());

                                dbRef.setValue(user);

                                Log.d(TAG, "user added");
                                final DatabaseReference nextNumber = database.getReference("nextNumber");
                                nextNumber.addListenerForSingleValueEvent(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d(TAG, "nextnumber listener called");

                                        String next = dataSnapshot.getValue(String.class);

                                        createAccounts(next, user.getCpr());
                                        Log.d(TAG, "user nextvalue called"+next);

                                        Long nextAccNumber = Long.parseLong(next);
                                        nextNumber.setValue("" + (nextAccNumber + 2));
                                        Log.d(TAG, "user "+ nextAccNumber);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                Intent accountOverviewIntent = new Intent(getApplicationContext(), AccountMenu.class);
                                startActivity(accountOverviewIntent);
                            } else{
                                Toast.makeText(getApplicationContext(), "This CPR is already registered, sure you don't have an account already?", Toast.LENGTH_LONG);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "The CPR number has to be exactly 10 characters", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "The password needs to be atleast 6 characters long", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_LONG).show();
        }
    }

    private void createAccounts(final String next, String cpr) {
        Long nextNumber = Long.parseLong(next);
        Log.d(TAG, "create accounts called"+ next +"    " + cpr);
        DatabaseReference ref = database.getReference("bankaccounts/");
        BankAccount defaultAccount = new BankAccount("Default", 0, "" + (nextNumber + 1));
        BankAccount budgetAccount = new BankAccount("Budget", 0, "" + (nextNumber + 2));

        ref.child("" + (nextNumber + 1)).setValue(defaultAccount);
        ref.child("" + (nextNumber + 2)).setValue(budgetAccount);

        DatabaseReference userRef = database.getReference("usersbankaccounts/" + cpr);
        userRef.child("" + (nextNumber + 1)).setValue("Default Account");
        userRef.child("" + (nextNumber + 2)).setValue("Budget Account");


    }

}
