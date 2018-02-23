package com.davidthigpen.receipttracker.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

/**
 * Created by david on 2/8/18.
 */
@Entity(tableName = "item_splitter_join",
        primaryKeys = { "receipt_item_id", "splitter_id" },
        foreignKeys = {
                @ForeignKey(entity = ReceiptItem.class,
                        parentColumns = "id",
                        childColumns = "receipt_item_id"),
                @ForeignKey(entity = Splitter.class,
                        parentColumns = "id",
                        childColumns = "splitter_id")
        })
public class ReceiptItemSplitterJoin {
    @NonNull
    @ColumnInfo(name = "receipt_item_id")
    private final String receiptItemId;
    @NonNull
    @ColumnInfo(name = "splitter_id")
    private final String splitterId;

    public ReceiptItemSplitterJoin(String receiptItemId, String splitterId) {
        this.receiptItemId = receiptItemId;
        this.splitterId = splitterId;
    }

    @NonNull
    public String getReceiptItemId() {
        return receiptItemId;
    }

    @NonNull
    public String getSplitterId() {
        return splitterId;
    }
}
