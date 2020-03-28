package com.tregz.miksing.data.user.tube;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.tube.Tube;
import com.tregz.miksing.data.user.User;

public class UserTubeRelation {
    @Embedded
    public UserTube join;
    @Relation(parentColumn = Tube.TABLE, entityColumn = DataNotation.ID)
    public Tube tube;
    @Relation(parentColumn = User.TABLE, entityColumn = DataNotation.ID)
    public User user;
}
