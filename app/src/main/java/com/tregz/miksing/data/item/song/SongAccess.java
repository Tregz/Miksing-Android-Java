package com.tregz.miksing.data.item.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
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
    LiveData<List<Song>> all();

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Update
    Single<Integer> update(Song...songs);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    LiveData<Song> query(String key);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    Maybe<Song> testQuery(String key);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(Song...songs);

    //@RawQuery(observedEntities = Song.class)
    //Observable<Song> item(SupportSQLiteQuery query);

}

