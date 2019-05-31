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

        LayoutInflater inflater =  Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout, null);
        init(view);


        Random rnd = new Random();
        List<Integer> easyIdList = new ArrayList<>();

        for (int i = 0; i <= 100; i++) {
            easyIdList.add(rnd.nextInt(999));
        }
        Log.d("TAG", ""+ easyIdList.get(rnd.nextInt(100)));
        showRandomId.setText(easyIdList.get(rnd.nextInt(100)));
        listener.applyText(Integer.parseInt(showRandomId.getText().toString()), Integer.parseInt(enteredRandomId.getText().toString()));

        builder.setView(view)

                .setNegativeButton("Cancel", ((DialogInterface dialog, int which) -> {

                }))
                .setPositiveButton("OK", ((DialogInterface dialog, int which) -> {

                        Intent returnToMenu = new Intent(getContext(), AccountMenu.class);
                        startActivity(returnToMenu);
                }));



        return builder.create();
    }

    public void init(View view){
        this.showRandomId = view.findViewById(R.id.randomID);
        this.enteredRandomId = view.findViewById(R.id.enteredRandomId);
    }

    public interface DialogListener{
        void applyText(int showRandomId, int enteredRandomId);
    }
}
