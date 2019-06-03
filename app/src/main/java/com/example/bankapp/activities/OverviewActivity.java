package com.example.bankapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.bankapp.entities.BankAccount;
import com.example.bankapp.entities.OverviewAdapter;
import com.example.bankapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OverviewActivity extends AppCompatActivity {

    // CLASS TAKEN FROM JESPER TANG PETERSEN
    public static final String TAG = "OVERVIEWACTIVITY";
    private FirebaseDatabase database;
    private RecyclerView rv_account_list;
    private ArrayList<BankAccount> accountsList = new ArrayList<>();
    private OverviewAdapter adapter = new OverviewAdapter(this, accountsList);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d(TAG, "onCreate called");
        init();
        loadAccounts();
        adapter.notifyDataSetChanged();

    }

    public void loadAccounts() {
        Intent getIntent = getIntent();
        String userCPR = getIntent.getStringExtra("CPR");
        DatabaseReference dbref = database.getReference("usersbankaccounts/" + userCPR);
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    DatabaseReference bankAccounts = database.getReference("bankaccounts/" + data.getKey());
                    Log.d(TAG, "key " + data.getKey());
                    Log.d(TAG, "value " + data.getValue());
                    bankAccounts.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d(TAG, "asdasdas" + dataSnapshot.getValue());
                            BankAccount bankAccount = dataSnapshot.getValue(BankAccount.class);
                            accountsList.add(bankAccount);
                            adapter.notifyDataSetChanged();

                            Log.d(TAG, "this is the array" + accountsList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        Log.d(TAG, "Init called");
        this.database = FirebaseDatabase.getInstance();
        this.rv_account_list = findViewById(R.id.accounts);
        this.rv_account_list.setAdapter(adapter);
        this.rv_account_list.setLayoutManager(new LinearLayoutManager(this));
    }
}