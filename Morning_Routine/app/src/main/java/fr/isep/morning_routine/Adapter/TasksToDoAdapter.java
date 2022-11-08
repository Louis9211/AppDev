package fr.isep.morning_routine.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import fr.isep.morning_routine.MainActivity;
import fr.isep.morning_routine.Model.TasksToDoModel;
import fr.isep.morning_routine.R;
import fr.isep.morning_routine.Utils;

public class TasksToDoAdapter extends RecyclerView.Adapter<TasksToDoAdapter.ViewHolder> {

    private List<TasksToDoModel> tasksToDoList;
    private MainActivity activity;

    public TasksToDoAdapter(MainActivity activity) {
        this.activity = activity;
        this.tasksToDoList = new ArrayList<>();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        TasksToDoModel item = tasksToDoList.get(position);
        holder.task.setText(item.getTask());

        String[] startingTimeArray = item.getStartingTime().split(":");
        String startingTime = numberToTwoDigits(startingTimeArray[0]) + "H " + numberToTwoDigits(startingTimeArray[1]);

        String[] endingTimeArray = item.getEndingTime().split(":");
        String endingTime = numberToTwoDigits(endingTimeArray[0]) + "H " + numberToTwoDigits(endingTimeArray[1]);

        holder.hour.setText(startingTime + " - " + endingTime);
        holder.task.setChecked(isNonNullNumber(item.getStatus()));

        holder.task.setOnClickListener((View view) -> {
            Utils utils = new Utils(view);
            List<TasksToDoModel> tasksToDoModels = utils.loadTodosFromMemory();
            TasksToDoModel task = tasksToDoModels.get(position);
            task.setStatus(isNonNullNumber(item.getStatus()) ? 0 : 1);
            try {
                utils.editTodoTask(position, task);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        if (item.getApplicationName().length() <= 1)
            holder.playButton.setVisibility(View.INVISIBLE);
        else {
            holder.playButton.setOnClickListener((View view) -> {
                Intent launchIntent = view.getContext().getPackageManager().getLaunchIntentForPackage(item.getApplicationName());
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(launchIntent);
                } else {
                    launchIntent = new Intent(Intent.ACTION_VIEW);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchIntent.setData(Uri.parse("market://details?id=" + item.getApplicationName()));
                    view.getContext().startActivity(launchIntent);
                }
            });
        }

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

    public void deleteToDoTask(int position) throws IOException {
        tasksToDoList.remove(position);
        notifyItemRemoved(position);
    }

    //The recyclerView will get the number of item it has to print
    public int getItemCount(){
    return tasksToDoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        TextView hour;
        ImageButton playButton;
        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.routineCheckBox);
            hour = view.findViewById(R.id.routineTime);
            playButton = view.findViewById(R.id.play);
        }
    }

    private String numberToTwoDigits(String number) {
        if (number.length() > 1)
            return number;

        return "0" + number;
    }

}
