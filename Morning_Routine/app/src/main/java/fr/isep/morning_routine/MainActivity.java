package fr.isep.morning_routine;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksToDoAdapter = new TasksToDoAdapter(this);
        tasksRecyclerView.setAdapter(tasksToDoAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksToDoAdapter, new StartActivity() {
            @Override
            public void run(Context context) {

                Intent switchActivityIntent = new Intent(context, ModifyToDoActivity.class);
                startActivity(switchActivityIntent);

            }
        }));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        loadTodoFromMemory();

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> switchCreateTodoActivity());
    }

    protected void loadTodoFromMemory() {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        Set<String> stringSet = sharedPref.getStringSet("tasks", new HashSet<>());
        tasksToDoList = new ArrayList<>();

        for (String string: stringSet) {
            try {
                TasksToDoModel deserializedElement = (TasksToDoModel) ObjectSerializer.deserialize(string);
                tasksToDoList.add(deserializedElement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tasksToDoAdapter.setTasksToDo(tasksToDoList);
    }



    protected void switchCreateTodoActivity() {
        Intent switchActivityIntent = new Intent(this, CreateTodoActivity.class);
        someActivityResultLauncher.launch(switchActivityIntent);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadTodoFromMemory();
                }
            });
}