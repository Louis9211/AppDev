package fr.isep.morning_routine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;

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
            alertDialogDeleteItem(viewHolder);
        } else {
            this.handleModify.run(this.adapter.getContext(), viewHolder.getAdapterPosition());
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
    }



    private void alertDialogDeleteItem(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.adapter.getContext());
        builder.setTitle("Supprimer la tâche");
        builder.setMessage("Voulez-vous vraiment supprimer cette tâche ?");
        builder.setPositiveButton("Supprimer", (dialogInterface, i) -> {
            try {
                this.adapter.deleteToDoTask((int) viewHolder.getAdapterPosition());
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

        ColorDrawable background;
        int BackgroundCornerOffset = 20;
        View itemView = viewHolder.itemView;
        if (horizontalDistance > 0) {
            background = new ColorDrawable(Color.CYAN);
        } else {
            background = new ColorDrawable(Color.RED);
        }
        if (horizontalDistance > 0) {
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) horizontalDistance) + BackgroundCornerOffset, itemView.getBottom());
        } else if (horizontalDistance < 0) {
            background.setBounds(itemView.getRight() + ((int) horizontalDistance) + BackgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else {
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(canvas);
    }
}
