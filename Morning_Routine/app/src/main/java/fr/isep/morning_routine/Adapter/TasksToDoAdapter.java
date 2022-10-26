package fr.isep.morning_routine.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.isep.morning_routine.MainActivity;
import fr.isep.morning_routine.Model.TasksToDoModel;
import fr.isep.morning_routine.ObjectSerializer;
import fr.isep.morning_routine.R;

public class TasksToDoAdapter extends RecyclerView.Adapter<TasksToDoAdapter.ViewHolder> {

    private List<TasksToDoModel> tasksToDoList;
    private MainActivity activity;

    public TasksToDoAdapter() {
        this.tasksToDoList = new ArrayList<>();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        TasksToDoModel item = tasksToDoList.get(position);
        //set the task to do in the holder
        holder.task.setText(item.getTask());
        holder.task.setChecked(isNonNullNumber(item.getStatus()));
    }
    //Return true is number non null
    private boolean isNonNullNumber(int n){
        return  n!=0;
    }

    //Set tasks in list
    public void setTasksToDo(List<TasksToDoModel> tasksToDoList){
        this.tasksToDoList = tasksToDoList;
        //in order to update the recyclerView
        notifyDataSetChanged();
    }

    public Context getContext(){return activity;}

    public void deleteToDoTask( int position) throws IOException {
        TasksToDoModel item = tasksToDoList.get(position);
        tasksToDoList.remove(position);
        notifyItemRemoved(position);

    }


    //The recyclerView will get the number of item it has to print
    public int getItemCount(){
    return tasksToDoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.routineCheckBox);
        }
    }


}
