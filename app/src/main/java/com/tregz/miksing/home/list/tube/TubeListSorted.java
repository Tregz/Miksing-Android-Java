package com.tregz.miksing.home.list.tube;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.tregz.miksing.base.list.ListSorted;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.tube.UserTube;
import com.tregz.miksing.data.user.tube.UserTubeRelation;

class TubeListSorted<T> extends ListSorted<T> {

    TubeListSorted(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override
    public boolean areContentsTheSame(T o1, T o2) {
        if (o1 instanceof UserTubeRelation) {
            Tube tube1 = ((UserTubeRelation) o1).tube;
            Tube tube2 = ((UserTubeRelation) o2).tube;
            if (tube1 != null && tube2 != null) return tube1.getUpdatedAt() == tube2.getUpdatedAt();
        }
        return false;
    }

    @Override
    public boolean areItemsTheSame(T o1, T o2) {
        if (o1 instanceof UserTubeRelation) {
            Tube tube1 = ((UserTubeRelation) o1).tube;
            Tube tube2 = ((UserTubeRelation) o2).tube;
            if (tube1 != null && tube2 != null) return compare(tube1.getId(), tube2.getId()) == 0;
        }
        return false;
    }

    @Override
    public int compare(T o1, T o2) {
        switch (comparator) {
            case ALPHA:
                if (o1 instanceof UserTubeRelation) {
                    Tube tube1 = ((UserTubeRelation) o1).tube;
                    Tube tube2 = ((UserTubeRelation) o2).tube;
                    if (tube1 != null && tube2 != null)
                        return compare(tube1.getName(), tube2.getName());
                }
            case DIGIT:
                if (o1 instanceof UserTubeRelation) {
                    UserTube join1 = ((UserTubeRelation) o1).join;
                    UserTube join2 = ((UserTubeRelation) o2).join;
                    if (join1 != null && join2 != null)
                        return join1.getPosition() - join2.getPosition();
                }
            case FRESH:
                if (o1 instanceof UserTubeRelation) {
                    Tube tube1 = ((UserTubeRelation) o1).tube;
                    Tube tube2 = ((UserTubeRelation) o2).tube;
                    if (tube1 != null && tube2 != null) {
                        Log.d(TAG, "UserTube: " + tube1.getCreatedAt());
                        Log.d(TAG, "UserTube: " + tube2.getCreatedAt());
                        return compare(tube1.getCreatedAt(), tube2.getCreatedAt());
                    }
                }
        }
        return 0;
    }

}
