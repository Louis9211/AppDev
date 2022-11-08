package fr.isep.morning_routine;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import fr.isep.morning_routine.Model.TasksToDoModel;


public class ModifyToDoActivity extends AppCompatActivity {

    private int elementId;

    public ModifyToDoActivity() {
        this.elementId = -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = findViewById(android.R.id.content).getRootView();
        setContentView(R.layout.activity_create_todo);
        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            this.elementId = extras.getInt("key");

        TimePicker startingHour = findViewById(R.id.newTodoStartingHour);
        startingHour.setIs24HourView(true);

        TimePicker endingHour = findViewById(R.id.newTodoEndingHour);
        endingHour.setIs24HourView(true);
        Utils utils = new Utils(rootView);
        List<TasksToDoModel> tasksToDoModels = utils.loadTodosFromMemory();
        TasksToDoModel task = tasksToDoModels.get(this.elementId);
        fillTodoForm(task);

        Button newTodoButton = findViewById(R.id.newToDoButton);
        newTodoButton.setOnClickListener(v -> {
            try {
                onSubmitForm(utils);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void onSubmitForm(Utils utils) throws IOException {
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
            utils.editTodoTask(this.elementId, tasksTodo);
            Intent intent = new Intent();
            intent.putExtra("newTask", ObjectSerializer.serialize(tasksTodo));
            setResult(RESULT_OK,intent);
            finish();
        } else {
            System.out.println("Erreur : champ vide");
        }
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

    //Convert Edittext to String
    private String ConvertToString(EditText text) {
        return text.getText().toString();
    }

}
