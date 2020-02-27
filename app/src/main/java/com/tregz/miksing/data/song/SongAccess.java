package com.tregz.miksing.data.song;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tregz.miksing.data.DataAccess;
import com.tregz.miksing.data.tube.song.TubeSongRelation;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface SongAccess extends DataAccess<Song> {
    String FROM_TABLE = " FROM " + Song.TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Query(SELECT_FROM_TABLE)
    LiveData<List<SongRelation>> all();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(Song...songs);

    @Update
    Single<Integer> update(Song...songs);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    LiveData<Song> whereId(String key);

    @Query(SELECT_FROM_TABLE + " WHERE id IN(SELECT song FROM tube_song WHERE tube = :id)")
    LiveData<List<SongRelation>> whereTube(String id);

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    Maybe<Song> test(String key);

    //@RawQuery(observedEntities = Song.class)
    //Observable<Song> item(SupportSQLiteQuery query);

}

