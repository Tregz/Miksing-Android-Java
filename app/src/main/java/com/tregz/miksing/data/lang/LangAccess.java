package com.tregz.miksing.data.lang;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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
public interface LangAccess extends DataAccess<Lang> {
    String FROM_TABLE = " FROM " + Lang.TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;

    @Delete
    Single<Integer> delete(Lang... data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insert(Lang... data);

    @Query(SELECT_FROM_TABLE + " WHERE id = :id")
    Maybe<Lang> whereId(String id);

    @Update
    Single<Integer> update(Lang... data);

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Query(DELETE_FROM_TABLE + " WHERE id = :id")
    Single<Integer> delete(String id);
}
