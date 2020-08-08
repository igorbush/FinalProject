package com.example.finalproject.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.models.TaskItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {

    private final CustomAdapterCallback listener;
    private List<TaskItem> arrayList;


    public TaskViewAdapter(ArrayList<TaskItem> arrayList, CustomAdapterCallback listener) {
        this.listener = listener;
        this.arrayList = arrayList;
    }

    public void setTasks(List<TaskItem> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CheckBox.OnCheckedChangeListener {
        public TextView name;
        public TextView dateTime;
        public TextView id;
        public CheckBox complete;

        private final CustomAdapterCallback listener;

        public TaskViewHolder(@NonNull View itemView, CustomAdapterCallback listener) {
            super(itemView);
            id = itemView.findViewById(R.id.id_task);
            name = itemView.findViewById(R.id.name_task);
            dateTime = itemView.findViewById(R.id.time_task);
            complete = itemView.findViewById(R.id.checkbox_task);
            this.listener = listener;
            this.itemView.setOnClickListener(this);
            this.complete.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listener.onCheckedChanged(getAdapterPosition(), buttonView, isChecked);
        }

    }

    public interface CustomAdapterCallback {
        void onItemClick(int pos);

        void onCheckedChanged(int pos, CompoundButton buttonView, boolean isChecked);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdTaskItem = R.layout.task_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdTaskItem, viewGroup, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view, listener);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        TaskItem taskItem = arrayList.get(i);
        taskViewHolder.id.setText(taskItem.getId());
        taskViewHolder.name.setText(taskItem.getTaskName());
        String dt = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(taskItem.getTaskDateTime());
        taskViewHolder.dateTime.setText(dt);
        taskViewHolder.complete.setChecked(taskItem.getComplete());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
