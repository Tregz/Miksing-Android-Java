package com.tregz.miksing.home.list.tube;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;
import androidx.recyclerview.widget.SortedListAdapterCallback;

import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.user.tube.UserTubeRelation;


public class TubeAdapter extends RecyclerView.Adapter<TubeHolder> {

    private SortedListAdapterCallback<UserTubeRelation> callback = new ListSorted<>(this);
    SortedList<UserTubeRelation> items = new SortedList<>(UserTubeRelation.class, callback);

    @Override
    public void onBindViewHolder(@NonNull TubeHolder holder, int position) {

    }

    @NonNull
    @Override
    public TubeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
