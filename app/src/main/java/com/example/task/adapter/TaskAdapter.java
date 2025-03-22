package com.example.task.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task.R;
import com.example.task.model.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onEditClick(Task task);
        void onDeleteClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.descriptionTextView.setText(task.getDescription());
        holder.assignedToTextView.setText("Assigned to: " + task.getAssignedTo());
        holder.statusTextView.setText("Status: " + task.getStatus());
        holder.deadlineTextView.setText("Deadline: " + task.getDeadline());
        holder.priorityTextView.setText("Priority: " + task.getPriority());

        holder.editButton.setOnClickListener(v -> listener.onEditClick(task));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, assignedToTextView, statusTextView, deadlineTextView, priorityTextView;
        Button editButton, deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            assignedToTextView = itemView.findViewById(R.id.assignedToTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            deadlineTextView = itemView.findViewById(R.id.deadlineTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}