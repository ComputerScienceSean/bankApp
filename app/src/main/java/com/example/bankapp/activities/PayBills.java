package com.example.bankapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.bankapp.R;
import com.example.bankapp.entities.BankAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PayBills extends AppCompatActivity {

    private Spinner accountFrom;
    FirebaseDatabase database;
    static ArrayList<BankAccount> accounts = new ArrayList<>();
    ArrayAdapter<BankAccount> adapter;
    public static final String TAG = "PAYBILLS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);
        init();
        loadAccounts();
    }

    public void init(){
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, accounts);
        this.accountFrom = findViewById(R.id.transferFromSpinner);
        this.database = FirebaseDatabase.getInstance();
        this.accountFrom.setAdapter(adapter);
    }

    public void loadAccounts () {
        Intent getIntent = getIntent();
        String userCPR = getIntent.getStringExtra("CPR");
        DatabaseReference dbref = database.getReference("usersbankaccounts/" + userCPR);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DatabaseReference bankaccounts = database.getReference("bankaccounts/" + data.getKey());
                    Log.d(TAG, data.getKey());
                    Log.d(TAG, "" + data.getValue());
                    bankaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BankAccount bankAccount = dataSnapshot.getValue(BankAccount.class);
                            accounts.add(bankAccount);
                            adapter.notifyDataSetChanged();

                            Log.d(TAG, "ACCOUNTS" + accounts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Log.d(TAG, "" + accounts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
