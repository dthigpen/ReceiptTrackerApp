package com.davidthigpen.receiptreader.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.davidthigpen.receiptreader.data.model.ReceiptItem;
import com.davidthigpen.receiptreader.data.util.DateConverter;

import java.util.List;

/**
 * Created by david on 2/8/18.
 */
@Dao
@TypeConverters(DateConverter.class)
public interface ReceiptItemDao {

    @Insert
    void insert(ReceiptItem receiptItem);

    @Insert
    void insert(List<ReceiptItem> receiptItems);

    @Update
    void update(ReceiptItem... receiptItems);

    @Delete
    void delete(ReceiptItem... receiptItems);

    @Query("DELETE FROM ReceiptItem")
    void deleteAll();

    @Query("SELECT * From ReceiptItem")
    List<ReceiptItem> getAllItems();

    @Query("SELECT * From ReceiptItem where id = :id LIMIT 1")
    ReceiptItem loadItemById(String id);

    @Query("SELECT * From ReceiptItem where receiptId = :id")
    List<ReceiptItem> loadAllByReceiptId(String id);
}
