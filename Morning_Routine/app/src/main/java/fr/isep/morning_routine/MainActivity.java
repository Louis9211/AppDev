package fr.isep.morning_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;
import fr.isep.morning_routine.Model.TasksToDoModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private TasksToDoAdapter tasksToDoAdapter;

    private List<TasksToDoModel> tasksToDoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tasksToDoList = new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksToDoAdapter = new TasksToDoAdapter(this);
        //Set the adapter to the recyclerView
        tasksRecyclerView.setAdapter(tasksToDoAdapter);

        TasksToDoModel tasksToDO = new TasksToDoModel();
        tasksToDO.setTask("test");
        tasksToDO.setStatus(0);
        tasksToDO.setId(1);

        //add the task to the tasksToDoList
        tasksToDoList.add(tasksToDO);
        tasksToDoList.add(tasksToDO);
        tasksToDoList.add(tasksToDO);
        tasksToDoList.add(tasksToDO);

        tasksToDoAdapter.setTasksToDo(tasksToDoList);

    }
}