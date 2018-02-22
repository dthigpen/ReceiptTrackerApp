package com.davidthigpen.receiptreader.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.davidthigpen.receiptreader.data.model.ReceiptItem;
import com.davidthigpen.receiptreader.data.model.ReceiptItemSplitterJoin;
import com.davidthigpen.receiptreader.data.model.Splitter;

import java.util.List;

/**
 * Created by david on 2/13/18.
 */

@Dao
public interface ItemSplitterJoinDao {

    @Insert
    void insert(ReceiptItemSplitterJoin receiptItemSplitterJoin);

    @Query("SELECT * FROM ReceiptItem INNER JOIN item_splitter_join ON ReceiptItem.id=item_splitter_join.receipt_item_id WHERE item_splitter_join.splitter_id=:splitterId")
    List<ReceiptItem> getReceiptItemsForSplitter(String splitterId);

    @Query("SELECT * FROM Splitter INNER JOIN item_splitter_join ON Splitter.id=item_splitter_join.splitter_id WHERE item_splitter_join.receipt_item_id=:receiptItemId")
    List<Splitter> getSplittersForReceiptItem(String receiptItemId);

}
