package com.example.bankapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.bankapp.R;

public class AccountMenu extends AppCompatActivity {

    private Button accountsButton, transferOwnAccButton, transferOtherAccButton, createNewAccButton, payBillsButton, resetPassButton;
    String userCPR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_menu);
        init();

    }

    public void seeAccounts(View view){


        Intent seeAccounts = new Intent(getApplicationContext(), OverviewActivity.class);
        seeAccounts.putExtra("CPR", userCPR);
        startActivity(seeAccounts);

    }

    public void setTransferOwnAccButton(View view){
        Intent setTransferOwnAccButton = new Intent(getApplicationContext(), TransferOwnAccount.class);
        setTransferOwnAccButton.putExtra("CPR", userCPR);
        startActivity(setTransferOwnAccButton);
    }

    public void setTransferOtherAccButton(View view){
        Intent setTransferOtherAccButton = new Intent(getApplicationContext(), TransferOtherAccount.class);
        setTransferOtherAccButton.putExtra("CPR", userCPR);
        startActivity(setTransferOtherAccButton);
    }

   /* public void setCreateNewAccButton(View view){
        Intent setCreateNewAccButton = new Intent(getApplicationContext(), CreateNewAccount.class);
        startActivity(setCreateNewAccButton);
    }

    public void setPayBillsButton(View view){
        Intent setPayBillsButton = new Intent(getApplicationContext(), PayBills.class);
        startActivity(setPayBillsButton);
    }
*/
    public void setResetPassButton(View view){
        Intent setResetPassButton = new Intent(getApplicationContext(), ResetPassword.class);
        setResetPassButton.putExtra("CPR", userCPR);
        startActivity(setResetPassButton);
    }


    public void init(){
        this.accountsButton = findViewById(R.id.accountsButton);
        this.transferOwnAccButton = findViewById(R.id.transferOwnAccButton);
        this.transferOtherAccButton = findViewById(R.id.transferOtherAccButton3);
        this.createNewAccButton = findViewById(R.id.createNewAccButton);
        this.payBillsButton = findViewById(R.id.payBillsButton);
        this.resetPassButton = findViewById(R.id.resetPassButton);
        Intent getIntent = getIntent();
        this.userCPR = getIntent.getStringExtra("CPR");

    }
}
