package com.tregz.miksing.home.list;

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
            @NonNull final RecyclerView.ViewHolder holder) {
        super.clearView(recycler, holder);
        view.onGestureClear(holder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder
    ) {
        int vertical = ListSorted.customOrder() ? ItemTouchHelper.UP | ItemTouchHelper.DOWN : 0;
        int horizontal = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(vertical, horizontal);
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        boolean alike = holder.getItemViewType() == target.getItemViewType();
        int holderPosition = holder.getAdapterPosition();
        int targetPosition = target.getAdapterPosition();
        boolean indexed = holderPosition > -1 && targetPosition > -1;
        return alike && indexed && view.onItemMoved(holderPosition, targetPosition);
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
