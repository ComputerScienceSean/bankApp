package com.example.bankapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.entities.AutoPay;
import com.example.bankapp.entities.BankAccount;
import com.example.bankapp.entities.EasyIdDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PayBills extends AppCompatActivity implements EasyIdDialog.DialogListener {

    private Spinner accountFrom;
    FirebaseDatabase database;
    private EditText transferAmount, paymentNumber;
    static ArrayList<BankAccount> accounts = new ArrayList<>();
    ArrayAdapter<BankAccount> adapter;
    public static final String TAG = "PAYBILLS";
    private int shownRandomId;
    private int enteredeRandomId;
    private CheckBox autoPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bills);
        init();
        loadAccounts();
    }

    public void goBack(View view){
        finish();
    }

    public void init(){
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, accounts);
        this.accountFrom = findViewById(R.id.transferFromSpinner);
        this.paymentNumber = findViewById(R.id.paymentNumber);
        this.transferAmount = findViewById(R.id.transferAmount);
        this.database = FirebaseDatabase.getInstance();
        this.accountFrom.setAdapter(adapter);
        this.autoPay = findViewById(R.id.autoPay);
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

    @Override
    public void applyText(int showRandomId, int enteredRandomId) {
        shownRandomId = showRandomId;
        enteredeRandomId = enteredRandomId;
        transferMoney();
    }

    public void openDialog(View view) {
        EasyIdDialog easyIdDialog = new EasyIdDialog();
        easyIdDialog.show(getSupportFragmentManager(), "easyID Dialog");
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
        String payToNumber = paymentNumber.getText().toString();

        Log.d(TAG, "*" + accFrom + "*");

        if (!accountFrom.getSelectedItem().toString().equals(paymentNumber.toString()) && autoPay.isChecked()) {
            transfer(accFrom, amount, false);
            transfer(payToNumber, amount, true);

            DatabaseReference autoPayreference = database.getReference("autopaymentforms/");
            AutoPay autoPay = new AutoPay(amount, accFrom, payToNumber, "date");
            autoPayreference.push().setValue(autoPay);

        } else if(!accountFrom.getSelectedItem().toString().equals(paymentNumber.toString()) && !autoPay.isChecked()) {
            transfer(accFrom, amount, false);
            transfer(payToNumber, amount, true);

        } else {
            Toast.makeText(getApplicationContext(), "You need to enter the number from your payment form, not an account", Toast.LENGTH_LONG).show();
        }
    }

}
