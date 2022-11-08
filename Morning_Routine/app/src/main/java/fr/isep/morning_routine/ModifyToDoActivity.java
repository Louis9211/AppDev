package fr.isep.morning_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;
import fr.isep.morning_routine.Model.TasksToDoModel;


public class ModifyToDoActivity extends AppCompatActivity {

    private TasksToDoAdapter adapter;
    private int elementId;

    public ModifyToDoActivity() {
        this.adapter = adapter;
        this.elementId = -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            this.elementId = extras.getInt("key");

        TasksToDoModel task = loadTodoFromMemory(this.elementId);
        fillTodoForm(task);

        Button newTodoButton = findViewById(R.id.newToDoButton);
        newTodoButton.setOnClickListener(v -> {
            try {
                onSubmitForm();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void onSubmitForm() throws IOException {
        EditText nameInput = findViewById(R.id.newTodoName);
        TimePicker startingTimeInput = findViewById(R.id.newTodoStartingHour);
        TimePicker endingTimeInput = findViewById(R.id.newTodoEndingHour);
        EditText applicationNameInput = findViewById(R.id.newToDoApplicationName);
        String taskName = ConvertToString(nameInput);
        String taskStartingTime = startingTimeInput.getHour() + ":" + startingTimeInput.getMinute();
        String taskEndingTime = endingTimeInput.getHour() + ":" + endingTimeInput.getMinute();
        String applicationName = ConvertToString(applicationNameInput);
        if (taskName.length() != 0) {
            TasksToDoModel tasksTodo = new TasksToDoModel(1, 0, taskName, taskStartingTime, taskEndingTime, applicationName);
            editTodoTask(tasksTodo);
            Intent intent = new Intent();
            intent.putExtra("newTask", ObjectSerializer.serialize(tasksTodo));
            setResult(RESULT_OK,intent);
            finish();
        } else {
            System.out.println("Erreur : champ vide");
            //TODO : display Popup here
        }
    }

    private void editTodoTask(TasksToDoModel taskToEdit) throws IOException {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> stringSet = sharedPref.getStringSet("tasks", new HashSet<>());

        Set<String> newStringSet = new HashSet<String>(stringSet);
        Object[] objects = newStringSet.toArray();
        newStringSet.remove(objects[this.elementId]);
        newStringSet.add(ObjectSerializer.serialize(taskToEdit));

        editor.putStringSet("tasks", newStringSet);
        editor.apply();
    }

    private void fillTodoForm(TasksToDoModel task) {
        EditText nameInput = findViewById(R.id.newTodoName);
        TimePicker startingTimeInput = findViewById(R.id.newTodoStartingHour);
        TimePicker endingTimeInput = findViewById(R.id.newTodoEndingHour);
        EditText applicationNameInput = findViewById(R.id.newToDoApplicationName);
        nameInput.setText(task.getTask());

        String[] startingTime = task.getStartingTime().split(":");
        startingTimeInput.setHour(Integer.parseInt(startingTime[0]));
        startingTimeInput.setMinute(Integer.parseInt(startingTime[1]));

        String[] endingTime = task.getEndingTime().split(":");
        endingTimeInput.setHour(Integer.parseInt(endingTime[0]));
        endingTimeInput.setMinute(Integer.parseInt(endingTime[1]));
        System.out.println(Arrays.toString(startingTime));
        System.out.println(Arrays.toString(endingTime));
        applicationNameInput.setText(task.getApplicationName());
    }

    protected TasksToDoModel loadTodoFromMemory(int id) {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        Set<String> stringSet = sharedPref.getStringSet("tasks", new HashSet<>());
        List<TasksToDoModel> tasksToDoList = new ArrayList<>();

        for (String string: stringSet) {
            try {
                TasksToDoModel deserializedElement = (TasksToDoModel) ObjectSerializer.deserialize(string);
                tasksToDoList.add(deserializedElement);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tasksToDoList.get(id);
    }




    //Convert Edittext to String
    private String ConvertToString(EditText text) {
        return text.getText().toString();
    }


}
