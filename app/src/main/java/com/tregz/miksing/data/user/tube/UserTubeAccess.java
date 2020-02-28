package com.tregz.miksing.data.user.tube;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.tregz.miksing.data.DataAccess;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.tube.song.TubeSong;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserTubeAccess extends DataAccess<UserTube> {
    String FROM_TABLE = " FROM " + UserTube.TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;
    String ORDER_BY_POSITION = " ORDER BY " + UserTube.TABLE + "." + DataNotation.SI;

    @Transaction
    @Query(SELECT_FROM_TABLE + ORDER_BY_POSITION)
    LiveData<List<UserTubeRelation>> all();

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    LiveData<UserTube> query(String key);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(UserTube...data);

    @Update
    Single<Integer> update(UserTube...data);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    Maybe<UserTube> whereId(String key);

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

}

