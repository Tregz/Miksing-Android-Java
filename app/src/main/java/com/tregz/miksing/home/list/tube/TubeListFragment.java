package com.tregz.miksing.home.list.tube;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.R;
import com.tregz.miksing.core.auth.AuthUtil;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.User;
import com.tregz.miksing.data.user.tube.UserTubeAccess;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListPosition;
import com.tregz.miksing.home.list.song.SongListFragment;

import java.util.Collections;
import java.util.List;

public class TubeListFragment extends ListFragment implements Observer<List<UserTubeRelation>> {
    private final String TAG = TubeListFragment.class.getSimpleName();

    private List<UserTubeRelation> relations;
    private TubeListFragment.OnItem onItem;
    private ListSorted.Order comparator = ListSorted.comparator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onItem = (TubeListFragment.OnItem) context;
        } catch (ClassCastException e) {
            String name = SongListFragment.OnItem.class.getSimpleName();
            throw new ClassCastException(context.toString() + " must implement " + name);
        }
    }

    @Override
    public void onChanged(List<UserTubeRelation> relations) {
        if (this.relations == null || this.relations.size() != relations.size()) {
            this.relations = relations;
            sort();
        } else {
            this.relations = relations;
            ((TubeListAdapter)adapter).items.replaceAll(relations);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext() != null) {
            adapter = new TubeListAdapter(getContext().getApplicationContext(), onItem);
            recycler.setAdapter(adapter);
            new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
        }
        access().all().observe(getViewLifecycleOwner(), this);
    }

    @Override
    public void onGestureClear(final int destination) {
        User user = !relations.isEmpty() ? relations.get(0).user : null;
        boolean editable = user != null && AuthUtil.isUser(user.getId());
        String userId = editable ? user.getId() : null;
        ListPosition list = new ListPosition(User.TABLE, userId, Tube.TABLE);
        adapter.notifyDataSetChanged(); // to update new sorted items order, before saving
        // Save new positions after gesture event
        boolean changed = false;
        for (int i = 0; i < relations.size(); i++) {
            UserTubeRelation relation = relations.get(i);
            if (list.hasChanged(relation.join, relation.join.getTubeId(), i)) {
                if (!changed) changed = true;
                if (!editable) new DataUpdate(access().update(relation.join));
            }
        }
        if (changed) {
            ((TubeListAdapter)adapter).items.replaceAll(relations); // update new positions
            if (!editable)  new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    toast(R.string.to_save_login);
                }
            });
        }
    }

    @Override
    public boolean onItemMoved(int from, int destination) {
        // Update unsorted array to save to new position after gesture event
        int start = Math.min(from, destination);
        int end = Math.max(from, destination);
        Log.d(TAG, "start" + start);
        Log.d(TAG, "end" + start);
        for (int i = start; i < end; i++)
            Collections.swap(relations, i, i + 1);
        Log.d(TAG, "swaped");
        // Update sorted list to animate gesture event
        adapter.notifyItemMoved(from, destination);
        return false;
    }

    @Override
    public void onItemSwipe(int position, int direction) {

    }

    @Override
    public void search(String query) {

    }

    @Override
    public void sort() {
        if (relations != null && adapter != null && recycler != null) {
            Log.d(TAG, "Comparator: " + comparator.name());
            //if (comparator != ListSorted.comparator) {
                comparator = ListSorted.comparator;
                //if (ListSorted.customOrder()) ordered();
                ((TubeListAdapter)adapter).items.replaceAll(relations);
                //recycler.smoothScrollToPosition(0);
            //}
        }
    }

    private UserTubeAccess access() {
        return DataReference.getInstance(getContext()).accessUserTube();
    }

    // Allow an interaction to be communicated to the activity
    public interface OnItem {
        void onItemClick(UserTubeRelation relation);

        void onItemLongClick(UserTubeRelation relation);
    }
}
