package com.tregz.miksing.data.tube.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface TubeSongAccess {
    String FROM_TABLE = " FROM " + TubeSong.TABLE;
    //String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Transaction
    @Query(SELECT_FROM_TABLE)
    LiveData<List<TubeSongRelation>> all();

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE user_tube = :tubeId") // ORDER BY user_song.spot
    LiveData<List<TubeSongRelation>> prepare(String tubeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(TubeSong...joins);

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE song = :key")
    LiveData<TubeSongRelation> query(String key);

    @Update //(onConflict = REPLACE)
    Single<Integer> update(TubeSong...joins);

    @RawQuery(observedEntities = TubeSong.class)
    LiveData<TubeSong> item(SupportSQLiteQuery query);

}