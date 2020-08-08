package com.example.finalproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.finalproject.R;
import com.example.finalproject.activities.MainActivity;
import com.example.finalproject.models.TaskItem;
import com.example.finalproject.common.TaskViewAdapter;
import com.example.finalproject.common.ToastNotify;
import com.example.finalproject.dialogs.AddTaskDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class TasksFragment extends Fragment implements TaskViewAdapter.CustomAdapterCallback {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addTaskButton;
    private FloatingActionButton recoverTaskButton;
    private FirebaseFirestore db;
    private List<TaskItem> taskItems;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        db = FirebaseFirestore.getInstance();
        if (this.getArguments() != null) {
            userId = this.getArguments().getString("userId");
        }
        recoverTaskButton = view.findViewById(R.id.recover_task_button);
        if (!MainActivity.taskStack.empty()) recoverTaskButton.setVisibility(View.VISIBLE);
        addTaskButton = view.findViewById(R.id.add_task_button);
        taskItems = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new TaskViewAdapter((ArrayList<TaskItem>) taskItems, this);
        recyclerView.setLayoutManager(layoutManager);
        db.collection("tasks").document(userId).collection("taskItems").orderBy("taskDateTime")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            taskItems = queryDocumentSnapshots.toObjects(TaskItem.class);
                            ((TaskViewAdapter) adapter).setTasks(taskItems);
                        }
                    }
                });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                deleteTask(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new AddTaskDialogFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                dialogFragment.setArguments(args);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "new");
            }
        });

        recoverTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskItem taskItem = MainActivity.taskStack.pop();
                DocumentReference doc = db.collection("tasks")
                        .document(userId)
                        .collection("taskItems")
                        .document(taskItem.getId());
                doc.set(taskItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ToastNotify.showShortMsg(getContext(), getString(R.string.recover_task));
                            if (MainActivity.taskStack.empty())
                                recoverTaskButton.setVisibility(View.INVISIBLE);
                        } else {
                            ToastNotify.showShortMsg(getContext(), getString(R.string.error_recover_task));
                        }
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int pos) {
        TaskItem taskItem = taskItems.get(pos);
        DialogFragment dialogFragment = new AddTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("id", taskItem.getId());
        args.putString("title", taskItem.getTaskName());
        args.putLong("datetime", taskItem.getTaskDateTime());
        dialogFragment.setArguments(args);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "edit");
    }

    @Override
    public void onCheckedChanged(int pos, CompoundButton buttonView, boolean isChecked) {
        TaskItem taskItem = taskItems.get(pos);
        db.collection("tasks")
                .document(userId)
                .collection("taskItems")
                .document(taskItem.getId()).update("complete", isChecked)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //ToastNotify.showShortMsg(getContext(), "check");
                        } else {
                            ToastNotify.showShortMsg(getContext(), getString(R.string.error_check_task));
                        }
                    }
                });
    }

    public void deleteTask(int pos) {
        TaskItem taskItem = taskItems.get(pos);
        db.collection("tasks")
                .document(userId)
                .collection("taskItems")
                .document(taskItem.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ToastNotify.showShortMsg(getContext(), getString(R.string.delete_task));
                        } else {
                            ToastNotify.showShortMsg(getContext(), getString(R.string.error_delete_task));
                        }
                    }
                });
        MainActivity.taskStack.push(taskItem);
        recoverTaskButton.setVisibility(View.VISIBLE);
    }
}
