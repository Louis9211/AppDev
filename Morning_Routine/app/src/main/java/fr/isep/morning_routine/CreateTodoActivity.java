package fr.isep.morning_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;
import fr.isep.morning_routine.Model.TasksToDoModel;


public class CreateTodoActivity extends AppCompatActivity {

    private RecyclerView tasksToDoRecyclerView;
    private TasksToDoAdapter tasksToDoAdapter;
    private List<TasksToDoModel> tasksToDoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        getSupportActionBar().hide();

        tasksToDoList = new ArrayList<>();

        Button newTodoButton = findViewById(R.id.newToDoButton);
        newTodoButton.setOnClickListener(v -> onSubmitForm());
    }

    private void onSubmitForm() {
        EditText nameInput = findViewById(R.id.newTodoName);
        EditText durationInput = findViewById(R.id.newTodoDuration);
        String taskName = ConvertToString(nameInput);
        String taskDuration = ConvertToString(durationInput);
        if (taskName.length() != 0) {
            System.out.println("Champ name rempli");
            tasksToDoRecyclerView = findViewById(R.id.tasksRecyclerView);
            tasksToDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            tasksToDoAdapter = new TasksToDoAdapter();
            //Set the adapter to the recyclerView
            tasksToDoRecyclerView.setAdapter(tasksToDoAdapter);
            TasksToDoModel tasksToDO = new TasksToDoModel();
            addTaskToList(tasksToDO, taskName, taskDuration);
            System.out.println(tasksToDoList);
        } else {
            //TODO : display Popup here
        }
        finish();
    }

    private void storeToDoTasks(){
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //Set the values
        Set<TasksToDoModel> set = new HashSet<TasksToDoModel>();
        set.addAll(tasksToDoList);
        //TODO : resoudre erreur set
        //editor.putStringSet("key", set);
        editor.commit();
    }

    private void addTaskToList(TasksToDoModel tasksToDo, String name, String duration) {
        tasksToDo.setTask(name);
        //tasksToDo.setDuration(duration);
        tasksToDo.setStatus(0);
        tasksToDo.setId(1);
        tasksToDoList.add(tasksToDo);
        tasksToDoAdapter.setTasksToDo(tasksToDoList);
    }

    //Convert Edittext to String
    private String ConvertToString(EditText text) {
        return text.getText().toString();
    }

    void saveToDoTasks(EditText text, String key) {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, text.getText().toString());
        editor.apply();
    }

    //Display when nothing is written and user try to add task.
    public void onErrorShowPopupClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //in order to delete popup when clicked outside
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // make quit the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }
}