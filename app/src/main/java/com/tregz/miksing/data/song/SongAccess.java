package com.tregz.miksing.data.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.tregz.miksing.data.DataAccess;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface SongAccess extends DataAccess<Song> {
    String FROM_TABLE = " FROM " + Song.TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Query(SELECT_FROM_TABLE)
    Maybe<List<Song>> songList();

    @Query(SELECT_FROM_TABLE)
    LiveData<List<Song>> liveSongList();

    @Transaction
    @Query(SELECT_FROM_TABLE)
    LiveData<List<SongRelation>> liveSongRelationList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(Song...songs);

    @Update
    Single<Integer> update(Song...songs);

    @Query(SELECT_FROM_TABLE + " WHERE id = :id")
    Maybe<Song> whereId(String id);

    @Query(SELECT_FROM_TABLE + " WHERE id = :id")
    LiveData<Song> liveWhereId(String id);

    @Transaction
    @Query(SELECT_FROM_TABLE + " WHERE id IN(SELECT song FROM tube_song WHERE tube = :tubeId)")
    LiveData<List<SongRelation>> whereTube(String tubeId);

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Query(DELETE_FROM_TABLE + " WHERE id NOT IN(SELECT song FROM tube_song)")
    Single<Integer> clear();

    @Query(SELECT_FROM_TABLE + " WHERE id = :id")
    Maybe<Song> test(String id);

    //@RawQuery(observedEntities = Song.class)
    //Observable<Song> item(SupportSQLiteQuery query);

}

