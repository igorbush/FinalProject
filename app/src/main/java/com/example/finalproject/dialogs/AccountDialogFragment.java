package com.example.finalproject.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.finalproject.R;
import com.example.finalproject.common.ToastNotify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountDialogFragment extends DialogFragment {
    private FirebaseFirestore db;
    private EditText editProfileName;
    private EditText editProfilePhone;
    private String userId;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        String dialogTitle = getString(R.string.dialog_edit_account);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_profile_dialog, null);
        editProfileName = view.findViewById(R.id.edit_profile_name_entry);
        editProfilePhone = view.findViewById(R.id.edit_profile_phone_entry);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
        String name = getArguments().getString("name");
        if (!name.equals("Not found") && !name.equals("Не найдено")) {
            editProfileName.setText(name);
        }
        String phone = getArguments().getString("phone");
        if (!phone.equals("Not found") && !phone.equals("Не найдено")) {
            editProfilePhone.setText(phone);
        }
        builder.setView(view);
        builder.setTitle(dialogTitle);
        builder.setPositiveButton(R.string.update_dialog_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newName = editProfileName.getText().toString().trim();
                String newPhone = editProfilePhone.getText().toString().trim();
                db.collection("users")
                        .document(userId)
                        .update("name", newName, "phone", newPhone)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    ToastNotify.showShortMsg(view.getContext(), view.getResources().getString(R.string.profile_updated));
                                } else {
                                    ToastNotify.showLongMsg(view.getContext(), view.getResources().getString(R.string.error_profile_update));
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton(R.string.cancel_dialog_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AccountDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
