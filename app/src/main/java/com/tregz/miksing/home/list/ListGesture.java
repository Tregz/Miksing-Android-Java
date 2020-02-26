package com.tregz.miksing.home.list;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.base.list.ListSorted;

public class ListGesture extends ItemTouchHelper.Callback {
    private String TAG = ListGesture.class.getSimpleName();

    private ListView view;

    public ListGesture(ListView view) {
        this.view = view;
    }

    @Override
    public void clearView(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder) {
        super.clearView(recycler, holder);
        Log.d(TAG, "clearView holder.getOldPosition():" + holder.getOldPosition());
        Log.d(TAG, "clearView holder.getAdapterPosition():" + holder.getAdapterPosition());
        if (holder.getOldPosition() != holder.getAdapterPosition())
        view.onGestureClear(holder.getOldPosition(), holder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder
    ) {
        if (ListSorted.customOrder()) {
            int vertical = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int horizontal = ItemTouchHelper.START | ItemTouchHelper.END;
            //return makeMovementFlags(vertical | horizontal, horizontal);
            return makeMovementFlags(vertical, horizontal);
        } else return 0;
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        view.onItemMoved(holder.getAdapterPosition(), target.getAdapterPosition());
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {
        view.onItemSwipe(holder.getAdapterPosition(), direction);
    }

    public enum Direction {
        RIGHT,
        START
    }
}
