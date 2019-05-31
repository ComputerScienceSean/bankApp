package com.example.bankapp.entities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bankapp.R;
import com.example.bankapp.activities.AccountMenu;
import com.example.bankapp.activities.TransferOtherAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class EasyIdDialog extends AppCompatDialogFragment {

    private TextView showRandomId;
    private EditText enteredRandomId;
    private DialogListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        init(view);


        Random rnd = new Random();
        List<Integer> easyIdList = new ArrayList<>();

        for (int i = 0; i <= 100; i++) {
            easyIdList.add(rnd.nextInt(9999));
        }

        int randomNum = rnd.nextInt(9999);
        this.showRandomId.setText(""+randomNum);


        builder.setView(view)
                .setMessage("RANDOM MSG :P")
                .setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> {

                }))
                .setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
                    int randomId = Integer.parseInt(showRandomId.getText().toString());
                    int enterRandomId = Integer.parseInt(enteredRandomId.getText().toString());
                    listener.applyText(randomId, enterRandomId);
                    TransferOtherAccount transferOtherAccount = new TransferOtherAccount();
                    transferOtherAccount.transferMoney();

                    Intent returnToMenu = new Intent(getContext(), AccountMenu.class);
                    startActivity(returnToMenu);
                }));


        return builder.create();
    }

    public void init(View view) {
        Random rnd = new Random();
        this.showRandomId = view.findViewById(R.id.randomID);
        this.enteredRandomId = view.findViewById(R.id.enteredRandomId);
//        this.showRandomId.setText(""+rnd.nextInt(9999));

    }

    public interface DialogListener {
        void applyText(int showRandomId, int enteredRandomId);
    }
}
