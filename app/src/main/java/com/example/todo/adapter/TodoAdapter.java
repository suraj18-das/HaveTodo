package com.example.todo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTask;
import com.example.todo.MainActivity;
import com.example.todo.Model.TodoModel;
import com.example.todo.R;
import com.example.todo.Utilities.DatabaseHelper;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    List<TodoModel> todos;
    MainActivity activity;
    private DatabaseHelper db;

    public TodoAdapter(DatabaseHelper db,MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = activity.getLayoutInflater().inflate(R.layout.task_layout, parent, false);
        return new TodoViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {
        db.openDatabase();
        TodoModel todo = todos.get(position);
        holder.task.setText(todo.getTask());
        holder.task.setChecked(todo.getStatus());
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    db.updateStatus(todo.getId(),1);
                }else {
                    db.updateStatus(todo.getId(),0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
    public void setTodos(List<TodoModel> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }
    public void editItem(int position) {
        TodoModel todo = todos.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", todo.getId());
        bundle.putString("task", todo.getTask());
        AddNewTask fragment=new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public void deleteItem(int position) {
        TodoModel todo = todos.get(position);
        db.deleteTask(todo.getId());
        todos.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext() {
        return activity;
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            task=itemView.findViewById(R.id.todoCheck);
        }
    }
}
