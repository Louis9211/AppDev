package fr.isep.morning_routine;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.isep.morning_routine.Model.TasksToDoModel;

public class Utils {
    View view;


    public Utils(View view) {
        this.view = view;
    }

    public List<TasksToDoModel> loadTodosFromMemory() {
        SharedPreferences sharedPref = this.view.getContext().getSharedPreferences("application", Context.MODE_PRIVATE);
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
        return tasksToDoList;
    }

    public void editTodoTask(int id, TasksToDoModel taskToEdit) throws IOException {
        SharedPreferences sharedPref = this.view.getContext().getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> stringSet = sharedPref.getStringSet("tasks", new HashSet<>());

        Set<String> newStringSet = new HashSet<String>(stringSet);
        Object[] objects = newStringSet.toArray();
        newStringSet.remove(objects[id]);
        newStringSet.add(ObjectSerializer.serialize(taskToEdit));

        editor.putStringSet("tasks", newStringSet);
        editor.apply();
    }

}
