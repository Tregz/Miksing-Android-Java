package com.tregz.miksing.data.tube;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.user.tube.UserTube;

public class TubeRelation {
    @Embedded
    public Tube tube;
    @Relation(parentColumn = DataNotation.PK, entityColumn = Tube.TABLE)
    public UserTube childUserTube;
}
