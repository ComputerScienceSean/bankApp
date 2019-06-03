package com.example.bankapp.activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.entities.BankAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateAccount extends AppCompatActivity {

    public static final String TAG = "CREATEACTIVITY";
    private FirebaseDatabase database;
    private Spinner accountToBeCreated;
    String userCPR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        init();
        loadAccounts();
    }

    public void loadAccounts(){
        ArrayList<String> accounts = new ArrayList<>();
        accounts.add("Savings Account");
        accounts.add("Business Account");
        accounts.add("Pension Account");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accounts);

        accountToBeCreated.setAdapter(adapter);
    }

    public void init(){
        this.accountToBeCreated = findViewById(R.id.accountToBeCreated);
        this.database = FirebaseDatabase.getInstance();
        Intent getIntent = getIntent();
        userCPR = getIntent.getStringExtra("CPR");
    }


    private void createAccounts(String next, String cpr) {

        // TODO: You can only create one new account, after program gets null in the checkSpinner ref

        DatabaseReference checkSpinner = database.getReference("usersbankaccounts/" + cpr);
        Log.d(TAG, ""+checkSpinner);
        checkSpinner.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    if (!data.child(accountToBeCreated.getSelectedItem().toString()).exists()){

                        Long nextNumber = Long.parseLong(next);
                        Log.d(TAG, "create accounts called "+ next +"   71 cpr_> " + cpr);
                        DatabaseReference ref = database.getReference("bankaccounts/");

                        BankAccount createdAccont = new BankAccount(accountToBeCreated.getSelectedItem().toString(), 100, "" + (nextNumber + 1));
                        ref.child("" + (nextNumber + 1)).setValue(createdAccont);

                        DatabaseReference userRef = database.getReference("usersbankaccounts/" + cpr);
                        userRef.child("" + (nextNumber + 1)).setValue(accountToBeCreated.getSelectedItem().toString());
                        Toast.makeText(getApplicationContext(), accountToBeCreated.getSelectedItem().toString() + " created!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You already have a " + accountToBeCreated.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getNextNumber(){
        DatabaseReference getNext = database.getReference("nextNumber");
        getNext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String next = dataSnapshot.getValue(String.class);

                Log.d(TAG, "1. user cpr " + userCPR);
                createAccounts(next, userCPR);
                Long nextAccNumber = Long.parseLong(next);
                getNext.setValue("" + (nextAccNumber + 1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void createAccountButton(View view){

        getNextNumber();
        Intent returnToMenu = new Intent(getApplicationContext(), AccountMenu.class);
        Log.d(TAG, "user cpr --> "+userCPR);
        returnToMenu.putExtra("CPR", userCPR);
        startActivity(returnToMenu);
    }

}
