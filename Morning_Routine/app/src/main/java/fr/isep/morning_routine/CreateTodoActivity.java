package fr.isep.morning_routine;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import fr.isep.morning_routine.Model.TasksToDoModel;


public class CreateTodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        getSupportActionBar().hide();

        TimePicker startingHour = findViewById(R.id.newTodoStartingHour);
        startingHour.setIs24HourView(true);

        TimePicker endingHour = findViewById(R.id.newTodoEndingHour);
        endingHour.setIs24HourView(true);

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
            storeToDoTask(tasksTodo);
            Intent intent = new Intent();
            intent.putExtra("newTask", ObjectSerializer.serialize(tasksTodo));
            setResult(RESULT_OK,intent);
            finish();
        } else {
            System.out.println("Erreur : champ vide");
            //TODO : display Popup here
        }
    }

    private void storeToDoTask(TasksToDoModel taskToAppend) throws IOException {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> stringSet = sharedPref.getStringSet("tasks", new HashSet<>());

        Set<String> newStringSet = new HashSet<String>(stringSet);
        newStringSet.add(ObjectSerializer.serialize(taskToAppend));

        editor.putStringSet("tasks", newStringSet);
        editor.apply();
    }



    //Convert Edittext to String
    private String ConvertToString(EditText text) {
        return text.getText().toString();
    }


}