package com.tregz.miksing.data.tube.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.tregz.miksing.data.DataAccess;
import com.tregz.miksing.data.DataNotation;
import com.tregz.miksing.data.tube.Tube;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TubeSongAccess extends DataAccess<TubeSong> {
    String FROM_TABLE = " FROM " + TubeSong.TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;
    String ORDER_BY_POSITION = " ORDER BY " + TubeSong.TABLE + "." + DataNotation.SI;

    @Transaction
    @Query(SELECT_FROM_TABLE)
    LiveData<List<TubeSongRelation>> all();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(TubeSong...joins);

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE song = :id")
    LiveData<TubeSongRelation> whereSong(String id);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    Maybe<TubeSong> whereId(String key);

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE tube = :id" + ORDER_BY_POSITION)
    LiveData<List<TubeSongRelation>> whereTube(String id);

    @Update //(onConflict = REPLACE)
    Single<Integer> update(TubeSong...joins);

    @Delete
    Single<Integer> delete(TubeSong...joins);

    @Query(DELETE_FROM_TABLE + " WHERE song = :songId AND tube = :tubeId")
    Single<Integer> delete(String songId, String tubeId);

    @RawQuery(observedEntities = TubeSong.class)
    LiveData<TubeSong> item(SupportSQLiteQuery query);

}
