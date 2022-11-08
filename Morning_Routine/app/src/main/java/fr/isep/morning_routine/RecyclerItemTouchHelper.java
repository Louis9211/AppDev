package fr.isep.morning_routine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;
import fr.isep.morning_routine.Model.TasksToDoModel;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TasksToDoAdapter adapter;
    private StartActivity handleModify;

    public RecyclerItemTouchHelper(TasksToDoAdapter adapter, StartActivity handleModify) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
        this.handleModify = handleModify;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT) {
            alertDialogDeleteItem(viewHolder, viewHolder.getAdapterPosition());
        } else {
            this.handleModify.run(this.adapter.getContext(), viewHolder.getAdapterPosition());
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    }


    private void alertDialogDeleteItem(final RecyclerView.ViewHolder viewHolder, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.adapter.getContext());
        builder.setTitle("Supprimer la tâche");
        builder.setMessage("Voulez-vous vraiment supprimer cette tâche ?");
        builder.setPositiveButton("Supprimer", (dialogInterface, i) -> {
            try {
                this.adapter.deleteToDoTask((int) viewHolder.getAdapterPosition());
                SharedPreferences sharedPref = adapter.getContext().getSharedPreferences("application", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                Set<String> stringSet = sharedPref.getStringSet("tasks", new HashSet<>());
                System.out.println(stringSet.toArray().length);
                Set<String> newStringSet = new HashSet<String>(stringSet);
                Object[] objects = newStringSet.toArray();
                newStringSet.remove(objects[position]);
                editor.putStringSet("tasks", newStringSet);
                editor.apply();
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float horizontalDistance, float verticalDistance, int actionState, boolean isActiveNow) {
        super.onChildDraw(canvas, recyclerView, viewHolder, horizontalDistance, verticalDistance, actionState, isActiveNow);
        Drawable icon;
        ColorDrawable background;
        int BackgroundCornerOffset = 20;
        View itemView = viewHolder.itemView;
        if (horizontalDistance > 0) {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit_24);
            background = new ColorDrawable(Color.CYAN);
        } else {
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete_24);
            background = new ColorDrawable(Color.RED);
        }
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();
        if (horizontalDistance > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) horizontalDistance) + BackgroundCornerOffset, itemView.getBottom());
        } else if (horizontalDistance < 0) {
            int iconRight = itemView.getRight() - iconMargin;
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + ((int) horizontalDistance) + BackgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(canvas);
        icon.draw(canvas);
    }
}
