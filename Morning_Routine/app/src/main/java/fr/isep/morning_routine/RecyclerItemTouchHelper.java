package fr.isep.morning_routine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.function.ToDoubleBiFunction;

import fr.isep.morning_routine.Adapter.TasksToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TasksToDoAdapter adapter;

    public RecyclerItemTouchHelper(TasksToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
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

        }
    }


    private void alertDialogDeleteItem(final RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.adapter.getContext());
        builder.setTitle("Supprimer la tâche");
        builder.setMessage("Voulez-vous vraiment supprimer cette tâche ?");
        builder.setPositiveButton("Supprimer", (dialogInterface, i) -> {
            try {
                System.out.println();
                this.adapter.deleteToDoTask((int) viewHolder.getAdapterPosition());
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float horizontalDistance, float verticalDistance, int actionState, boolean isActiveNow) {
        super.onChildDraw(canvas, recyclerView, viewHolder, horizontalDistance, verticalDistance, actionState, isActiveNow);

        ColorDrawable background;
        int BackgroundCornerOffset = 20;
        View itemView = viewHolder.itemView;
        if(horizontalDistance>0){
            background = new ColorDrawable(Color.CYAN);
        }
        else{
            background = new ColorDrawable(Color.RED);
        }
        if (horizontalDistance > 0) {
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)horizontalDistance) + BackgroundCornerOffset, itemView.getBottom());
        } else if (horizontalDistance < 0) {
            background.setBounds(itemView.getRight() + ((int) horizontalDistance) + BackgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
        else{
            background.setBounds(0,0,0,0);
        }
        background.draw(canvas);

    }
}
