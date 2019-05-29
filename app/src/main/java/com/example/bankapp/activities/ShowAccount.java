package com.example.bankapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.entities.BankAccount;

public class ShowAccount extends AppCompatActivity {

    private TextView balance;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account);

        Intent getIntent = getIntent();
        BankAccount bankAccount = getIntent.getParcelableExtra("Account");


        this.balance = findViewById(R.id.balance);
        this.returnButton = findViewById(R.id.returnButton);
        balance.setText(""+bankAccount.getBalance());
    }

    public void goBack(View view){
        Intent goBackIntent = new Intent(getApplicationContext(), AccountMenu.class);
        startActivity(goBackIntent);
    }
}
