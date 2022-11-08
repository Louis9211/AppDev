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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;
import fr.isep.morning_routine.Model.TasksToDoModel;


public class ModifyToDoActivity extends AppCompatActivity {

    private TasksToDoAdapter adapter;

    public ModifyToDoActivity() {
        this.adapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        getSupportActionBar().hide();

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
        EditText durationInput = findViewById(R.id.newTodoDuration);
        String taskName = ConvertToString(nameInput);
        String taskDuration = ConvertToString(durationInput);

        if (taskName.length() != 0) {
            System.out.println("Champ name rempli");
            TasksToDoModel tasksTodo = new TasksToDoModel();
            tasksTodo.setStatus(0);
            tasksTodo.setTask(taskName);
            tasksTodo.setId(10);
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

    public void modifyToDo(final RecyclerView.ViewHolder viewHolder){
        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
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