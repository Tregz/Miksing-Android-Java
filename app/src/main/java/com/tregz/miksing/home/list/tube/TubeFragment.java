package com.tregz.miksing.home.list.tube;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.DataReference;
import com.tregz.miksing.data.user.tube.UserTubeAccess;
import com.tregz.miksing.data.user.tube.UserTubeRelation;
import com.tregz.miksing.data.user.tube.UserTubeUpdate;
import com.tregz.miksing.home.list.ListFragment;
import com.tregz.miksing.home.list.ListGesture;
import com.tregz.miksing.home.list.ListView;

import java.util.List;

public class TubeFragment extends ListFragment implements Observer<List<UserTubeRelation>>,
        ListView {

    private List<UserTubeRelation> relations;
    private int destination = 0; // last gesture's target position

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserTubeAccess access = DataReference.getInstance(getContext()).accessUserTube();
        access.all().observe(this, this);
    }

    @Override
    public void onChanged(List<UserTubeRelation> relations) {
        if (this.relations == null || this.relations.size() != relations.size()) {
            this.relations = relations;
            sort();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TubeAdapter();
        recycler.setAdapter(adapter);
        new ItemTouchHelper(new ListGesture(this)).attachToRecyclerView(recycler);
    }

    @Override
    public void onGestureClear(int from, int destination) {

    }

    @Override
    public void onItemMoved(int from, int destination) {

    }

    @Override
    public void onItemSwipe(int position, int direction) {

    }

    @Override
    public void ordered() {
        ((TubeAdapter)adapter).items.beginBatchedUpdates();
        for (int i = 0; i < relations.size(); i++) {
            UserTubeRelation relation = relations.get(i);
            if (relation.join.getPosition() != i) {
                relation.join.setPosition(i);
                new UserTubeUpdate(getContext(), relation.join);
            }
        }
        ((TubeAdapter)adapter).items.endBatchedUpdates();
        recycler.smoothScrollToPosition(destination);
    }

    @Override
    public void save(String name) {

    }

    @Override
    public void search(String query) {

    }

    @Override
    public void sort() {
        if (relations != null && adapter != null && recycler != null) {
            destination = 0;
            if (ListSorted.customOrder()) ordered();
            ((TubeAdapter)adapter).items.replaceAll(relations);
            recycler.smoothScrollToPosition(0);
        }
    }
}
