package com.example.bankapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.myViewHolder> {
    Context mContext;
    List<BankAccount> mData;


    public OverviewAdapter(Context mContext, List<BankAccount> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.overview_card_item, viewGroup, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        myViewHolder.accountName.setText(mData.get(i).getTitle());
        myViewHolder.accountNo.setText(""+ mData.get(i).getAccNumber());
        myViewHolder.accountBal.setText("" + mData.get(i).getBalance());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private CardView cv_cardItem;
        private TextView accountName, accountNo, accountBal;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.accountName);
            accountNo = itemView.findViewById(R.id.accountNumber);
            accountBal = itemView.findViewById(R.id.accountBalance);
            cv_cardItem = itemView.findViewById(R.id.cv_cardItem);

        }
    }
}