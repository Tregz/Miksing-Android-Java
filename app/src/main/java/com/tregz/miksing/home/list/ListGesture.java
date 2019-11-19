package com.tregz.miksing.home.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.base.list.ListSorted;

public class ListGesture extends ItemTouchHelper.Callback {
    //private String TAG = ListGesture.class.getSimpleName();

    private ListView view;

    ListGesture(ListView view) {
        this.view = view;
    }

    @Override
    public void clearView(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder) {
        super.clearView(recycler, holder);
        view.onGestureClear(holder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder
    ) {
        if (ListSorted.customOrder()) {
            int vertical = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int horizontal = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(vertical | horizontal, 0);
        } else return 0;
    }

    @Override
    public boolean onMove(
            @NonNull RecyclerView recycler,
            @NonNull RecyclerView.ViewHolder holder,
            @NonNull RecyclerView.ViewHolder target
    ) {
        int from = holder.getAdapterPosition();
        int to = target.getAdapterPosition();
        if (from >= 0) view.onItemMoved(from, to);
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder holder, int direction) {
        // do nothing
    }
}
