package com.example.bankapp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.R;
import com.example.bankapp.AccountMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ResetPassword extends AppCompatActivity {

    private EditText password, passwordConfirm;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
    }

    public void updatePassword(View view){
        if(password.getText().toString().equals(passwordConfirm.getText().toString()) && password.getText().toString().length() >= 6){
            Intent getIntent = getIntent();
            String userCPR = getIntent.getStringExtra("CPR");
            DatabaseReference dbref = database.getReference("users/" + userCPR + "/password");
            dbref.setValue(passwordConfirm.getText().toString());
            Intent returnToMenu = new Intent(getApplicationContext(), AccountMenu.class);
            startActivity(returnToMenu);

        } else {
            Toast.makeText(getApplicationContext(), "The password is either too short or doesn't match. It must be atleast 6 characters!", Toast.LENGTH_LONG).show();
        }
    }

    public void init(){
        this.password = findViewById(R.id.newPass);
        this.passwordConfirm = findViewById(R.id.newPassConfirm);
        this.database = FirebaseDatabase.getInstance();

    }
}
