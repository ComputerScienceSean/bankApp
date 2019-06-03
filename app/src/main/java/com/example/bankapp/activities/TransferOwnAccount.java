package com.example.bankapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankapp.entities.BankAccount;
import com.example.bankapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransferOwnAccount extends AppCompatActivity {

    private Spinner accountFrom, accountTo;
    private EditText transferAmount;
    private FirebaseDatabase database;
    static ArrayList<BankAccount> accounts = new ArrayList<>();
    ArrayAdapter<BankAccount> adapter;

    public static final String TAG = "TRANSFEROWNACCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_own_account);
        init();
        loadAccounts();

    }

    public void goBack(View view){
        finish();
    }


    public void transfer( String accountNumber,  Double amount,  Boolean add) {
        DatabaseReference dbref = database.getReference("bankaccounts");
        dbref.child(accountNumber).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Double temp = dataSnapshot.getValue(Double.class);
                    if (amount > temp && !add) {
                        Toast.makeText(getApplicationContext(), "You dont have enough money to do that!", Toast.LENGTH_LONG).show();
                    } else {

                        if (add) {
                            dbref.child(accountNumber).child("balance").setValue((temp + amount));
                        } else {
                            dbref.child(accountNumber).child("balance").setValue((temp - amount));
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void transferMoney(View view) {

        if (transferAmount.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Invalid amount to transfer entered. Try again!", Toast.LENGTH_LONG).show();
            return;
        }
        Double amount = Double.parseDouble(transferAmount.getText().toString());

        String accFrom = accountFrom.getSelectedItem().toString().substring(accountFrom.getSelectedItem().toString().lastIndexOf(" " ) + 1);
        String accTo = accountTo.getSelectedItem().toString().substring(accountTo.getSelectedItem().toString().lastIndexOf(" ") + 1);

        Log.d(TAG, "*" + accFrom + "*");


        if (!accountFrom.getSelectedItem().toString().equals(accountTo.getSelectedItem().toString())) {
            transfer(accFrom, amount, false);
            transfer(accTo, amount, true);

            Intent returnToMenu = new Intent(getApplicationContext(), AccountMenu.class);
            startActivity(returnToMenu);
        } else {
            Toast.makeText(getApplicationContext(), "You can't transfer money between the same account", Toast.LENGTH_LONG).show();
        }
    }


    public void init () {
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, accounts);
        this.accountFrom = findViewById(R.id.transferFromSpinner);
        this.accountTo = findViewById(R.id.transferToSpinner);
        this.transferAmount = findViewById(R.id.transferAmount);
        this.database = FirebaseDatabase.getInstance();
        this.accountFrom.setAdapter(adapter);
        this.accountTo.setAdapter(adapter);
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
                    Log.d("test", data.getKey());
                    bankaccounts.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            BankAccount bankAccount = dataSnapshot.getValue(BankAccount.class);
                            accounts.add(bankAccount);
                            adapter.notifyDataSetChanged();

                            Log.d("test", "" + accounts);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Log.d("test", "" + accounts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
