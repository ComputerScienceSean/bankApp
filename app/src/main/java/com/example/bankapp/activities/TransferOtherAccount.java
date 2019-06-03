package com.example.bankapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.entities.BankAccount;
import com.example.bankapp.entities.EasyIdDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransferOtherAccount extends AppCompatActivity implements EasyIdDialog.DialogListener {


    private Spinner accountFrom;
    private EditText transferAmount, accountTo;
    private FirebaseDatabase database;
    private Button transferButton;
    static ArrayList<BankAccount> accounts = new ArrayList<>();
    ArrayAdapter<BankAccount> adapter;
    private int shownRandomId;
    private int enteredeRandomId;

    public static final String TAG = "TRANSFEROTHERACCOUNT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_other_account);
        init();
        loadAccounts();

    }


    public void transfer(String accountNumber, Long amount, Boolean add) {
        DatabaseReference dbref = database.getReference("bankaccounts");
        dbref.child(accountNumber).child("balance").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Long temp = dataSnapshot.getValue(Long.class);
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


    public void transferMoney() {

        Long amount = Long.parseLong(transferAmount.getText().toString());

        String accFrom = accountFrom.getSelectedItem().toString().substring(accountFrom.getSelectedItem().toString().lastIndexOf(" ") + 1);
        String accTo = accountTo.getText().toString();

        Log.d(TAG, "*" + accFrom + "*");

        if (!accountFrom.getSelectedItem().toString().equals(accountTo.toString())) {
            transfer(accFrom, amount, false);
            transfer(accTo, amount, true);


        } else {
            Toast.makeText(getApplicationContext(), "You can't transfer money between the same account", Toast.LENGTH_LONG).show();
        }
    }


    public void init() {
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, accounts);
        this.accountFrom = findViewById(R.id.transferFromSpinner);
        this.accountTo = findViewById(R.id.accountnumberRecipient);
        this.transferAmount = findViewById(R.id.transferAmount);
        this.database = FirebaseDatabase.getInstance();
        this.accountFrom.setAdapter(adapter);
        this.transferButton = findViewById(R.id.transferOtherAccButton3);
    }

    public void openDialog(View view) {
        EasyIdDialog easyIdDialog = new EasyIdDialog();
        easyIdDialog.show(getSupportFragmentManager(), "easyID Dialog");
    }


    public void loadAccounts() {
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


    @Override
    public void applyText(int showRandomId, int enteredRandomId) {
        shownRandomId = showRandomId;
        enteredeRandomId = enteredRandomId;
        transferMoney();
    }
}
