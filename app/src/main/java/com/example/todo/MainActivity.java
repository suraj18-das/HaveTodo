package com.example.todo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Model.TodoModel;
import com.example.todo.Utilities.DatabaseHelper;
import com.example.todo.Utilities.RecyclerTouchHelper;
import com.example.todo.adapter.TodoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListner {

    private RecyclerView taskRecyclerView;
    private TodoAdapter taskAdapter;
    private List<TodoModel> taskList;
    DatabaseHelper db;
    private FloatingActionButton add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        db=new DatabaseHelper(this);
        db.openDatabase();
        taskList=new ArrayList<>();

        taskRecyclerView=findViewById(R.id.taskView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter=new TodoAdapter(db,this);
        taskRecyclerView.setAdapter(taskAdapter);

        add=findViewById(R.id.add);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTodos(taskList);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTodos(taskList);
        taskAdapter.notifyDataSetChanged();
    }
}