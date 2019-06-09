package com.example.bankapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bankapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText cpr, password;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        if (savedInstanceState != null){
            cpr.setText(savedInstanceState.getString("cpr"));
            password.setText(savedInstanceState.getString("password"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("cpr", cpr.getText().toString());
        outState.putString("password", password.getText().toString());
    }

    public void init() {
        this.cpr = findViewById(R.id.CPR);
        this.password = findViewById(R.id.password);
        this.database = FirebaseDatabase.getInstance();
    }

    public void registerIntent(View view){
        Intent register = new Intent(this, RegisterUser.class);
        startActivity(register);

    }

    public void login(View view){
        DatabaseReference dbRef = database.getReference("users/" + cpr.getText().toString());
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    if (dataSnapshot.child("password").getValue().equals(password.getText().toString()) && dataSnapshot.hasChild("password")){
                        Intent login = new Intent(getApplicationContext(), AccountMenu.class);
                        login.putExtra("CPR", cpr.getText().toString());
                        startActivity(login);

                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect login credentials, Try again", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No user with that CPR number exists", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
