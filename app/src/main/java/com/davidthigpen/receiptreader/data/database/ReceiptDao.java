package com.davidthigpen.receiptreader.data.database;//package com.davidthigpen.roomwithdata.data.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.davidthigpen.receiptreader.data.model.Receipt;
import com.davidthigpen.receiptreader.data.model.ReceiptItemSplitterJoin;
import com.davidthigpen.receiptreader.data.util.DateConverter;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by david on 2/8/18.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface ReceiptDao {

    @Query("SELECT * From Receipt")
    List<Receipt> getAllReceipts();

    @Query("SELECT * FROM Receipt WHERE id IS :receiptId LIMIT 1")
    Receipt loadById(String receiptId);

    @Query("SELECT * FROM Receipt WHERE store_name LIKE :storeName")
    List<Receipt> loadAllByStoreName(String storeName);

    @Insert()
    void insertReceipt(Receipt mReceipt);

    @Update(onConflict = REPLACE)
    void updateReceipt(Receipt receipt);

    @Insert(onConflict = IGNORE)
    void insertReceipt(List<Receipt> mReceipt);

    @Delete
    void delete(Receipt mReceipt);

    @Query("DELETE FROM Receipt")
    void deleteAll();

}