package com.tregz.miksing.data.user.list;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tregz.miksing.data.DataAccess;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UserListAccess extends DataAccess<UserList> {
    String FROM_TABLE = " FROM " + UserList.TABLE;
    String DELETE_FROM_TABLE = "DELETE" + FROM_TABLE;
    String SELECT_FROM_TABLE = "SELECT *" + FROM_TABLE;

    @Query(SELECT_FROM_TABLE)
    LiveData<List<UserList>> all();

    @Query(DELETE_FROM_TABLE)
    Single<Integer> wipe();

    @Update
    Single<Integer> update(UserList... songs);

    @Query(SELECT_FROM_TABLE + " WHERE id = :key")
    LiveData<UserList> query(String key);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Single<List<Long>> insert(UserList... songs);

}

