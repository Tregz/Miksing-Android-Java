package com.tregz.miksing.home.list.tube;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tregz.miksing.arch.auth.AuthUtil;
import com.tregz.miksing.arch.pref.PrefShared;
import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.DataUpdate;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.User;
import com.tregz.miksing.data.user.tube.UserTube;
import com.tregz.miksing.data.user.tube.UserTubeAccess;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListView;
import com.tregz.miksing.home.list.song.SongListFragment;

import java.util.Collections;
import java.util.List;

public class TubeListFragment extends ListFragment implements Observer<List<UserTubeRelation>>,
        ListView {
    private final String TAG = TubeListFragment.class.getSimpleName();

    private List<UserTubeRelation> relations;
    private int destination = 0; // last gesture's target position
    private TubeListFragment.OnItem onItem;
    //private boolean reordered = false;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        access().all().observe(getViewLifecycleOwner(), this);
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
    }

    @Override
    public void onGestureClear(int from, int destination) {
        //((TubeListAdapter)adapter).items.beginBatchedUpdates();
        boolean changed = false;
        for (int i = 0; i < relations.size(); i++) {
            UserTubeRelation relation = relations.get(i);
            if (relation.join.getPosition() != i) {
                if (!changed) changed = true;
                relation.join.setPosition(i);
                if (AuthUtil.logged() && relation.user.getId().equals(AuthUtil.user().getUid())) {
                    DatabaseReference user = FirebaseDatabase.getInstance().getReference(User.TABLE);
                    DatabaseReference tube = user.child(relation.user.getId()).child(Tube.TABLE);
                    tube.child(relation.tube.getId()).setValue(relation.join.getPosition());
                } else new DataUpdate(access().update(relation.join));
            }
        }
        //((TubeListAdapter)adapter).items.endBatchedUpdates();
        if (changed) {
            adapter.notifyDataSetChanged();
            ((TubeListAdapter)adapter).items.replaceAll(relations);
            recycler.smoothScrollToPosition(destination);
        }
    }

    @Override
    public void onItemMoved(int from, int destination) {
        // Update unsorted array to save to new position after gesture event
        this.destination = destination;
        int start = Math.min(from, destination);
        int end = Math.max(from, destination);
        Log.d(TAG, "start" + start);
        Log.d(TAG, "end" + start);
        for (int i = start; i < end; i++)
            Collections.swap(relations, i, i + 1);
        Log.d(TAG, "swaped");
        // Update sorted list to animate gesture event
        adapter.notifyItemMoved(from, destination);
    }

    @Override
    public void onItemSwipe(int position, int direction) {

    }

    @Override
    public void save(String name, boolean paste) {

    }

    @Override
    public void search(String query) {

    }

    @Override
    public void sort() {
        if (relations != null && adapter != null && recycler != null) {
            Log.d(TAG, "Comparator: " + comparator.name());
            if (comparator != ListSorted.comparator) {
                destination = 0;
                comparator = ListSorted.comparator;
                //if (ListSorted.customOrder()) ordered();
                ((TubeListAdapter)adapter).items.replaceAll(relations);
                //recycler.smoothScrollToPosition(destination);
            }
        }
    }

    private UserTubeAccess access() {
        return DataReference.getInstance(getContext()).accessUserTube();
    }

    // Allow an interaction to be communicated to the activity
    public interface OnItem {
        void onItemClick(UserTube tube, String title);

        void onItemLongClick(UserTube tube);
    }
}
