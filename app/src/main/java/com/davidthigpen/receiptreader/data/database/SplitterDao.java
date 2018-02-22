package com.davidthigpen.receiptreader.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.davidthigpen.receiptreader.data.model.Splitter;

import java.util.List;

/**
 * Created by david on 2/13/18.
 */

@Dao
public interface SplitterDao {

    @Insert
    void insert(Splitter splitter);

    @Update
    void update(Splitter... splitters);

    @Delete
    void delete(Splitter... splitters);

    @Query("DELETE FROM splitter")
    void deleteAll();

    @Query("SELECT * From Splitter")
    List<Splitter> getAllSplitters();

    @Query("SELECT * From Splitter where id = :id LIMIT 1")
    Splitter loadSplitterById(String id);
}
