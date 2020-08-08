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
import com.example.finalproject.models.TaskItem;
import com.example.finalproject.common.ToastNotify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class AddTaskDialogFragment extends DialogFragment {
    private FirebaseFirestore db;
    private EditText editTaskTitle;
    private MaskedEditText editTaskDate;
    private MaskedEditText editTaskTime;
    private String userId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        final String tag = getTag();
        String dialogTitle = this.getResources().getString(R.string.dialog_add_task);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
        int positiveButtonTextId = R.string.add_dialog_button;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_task_dialog, null);
        editTaskTitle = view.findViewById(R.id.edit_task_title);
        editTaskDate = view.findViewById(R.id.edit_task_date);
        editTaskTime = view.findViewById(R.id.edit_task_time);
        if (tag.equals("edit")) {
            positiveButtonTextId = R.string.update_dialog_button;
            dialogTitle = getActivity().getResources().getString(R.string.dialog_update_task);
            editTaskTitle.setText(getArguments().getString("title"));
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(getArguments().getLong("datetime"));
            String[] dtArray = timeStamp.split(" ");
            editTaskDate.setText(dtArray[0]);
            editTaskTime.setText(dtArray[1]);

        }
        builder.setView(view);
        builder.setTitle(dialogTitle);
        builder.setPositiveButton(positiveButtonTextId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                DocumentReference doc;
                if (tag.equals("edit")) {
                    doc = db.collection("tasks")
                            .document(userId)
                            .collection("taskItems")
                            .document(getArguments().getString("id"));
                } else {
                    doc = db.collection("tasks")
                            .document(userId)
                            .collection("taskItems")
                            .document();
                }
                String title = editTaskTitle.getText().toString();
                String date = editTaskDate.getText().toString();
                String time = editTaskTime.getText().toString();
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                long dateTime = 0;
                try {
                    dateTime = simpleDateFormat.parse(date + " " + time).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                TaskItem taskItem = new TaskItem(doc.getId(), title, dateTime, false);
                doc.set(taskItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ToastNotify.showShortMsg(view.getContext(), view.getResources().getString(R.string.saved_task));
                        } else {
                            ToastNotify.showShortMsg(view.getContext(), view.getResources().getString(R.string.error_save_task));
                        }
                    }
                });
            }
        });
        builder.setNegativeButton(R.string.cancel_dialog_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AddTaskDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}
