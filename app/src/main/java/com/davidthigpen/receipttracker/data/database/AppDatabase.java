package com.davidthigpen.receipttracker.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.davidthigpen.receipttracker.data.model.Receipt;
import com.davidthigpen.receipttracker.data.model.ReceiptItem;
import com.davidthigpen.receipttracker.data.model.ReceiptItemSplitterJoin;
import com.davidthigpen.receipttracker.data.model.Splitter;
import com.davidthigpen.receipttracker.data.util.DateConverter;

/**
 * Created by david on 2/8/18.
 */

@Database(entities = {Receipt.class,ReceiptItem.class,Splitter.class, ReceiptItemSplitterJoin.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase INSTANCE;

    public abstract ReceiptDao getReceiptDao();
    public abstract ReceiptItemDao getReceiptItemDao();
    public abstract SplitterDao getSplitterDao();
    public abstract ItemSplitterJoinDao getItemSplitterJoinDao();

    public static AppDatabase getInMemoryDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class).build();
        }
        return INSTANCE;
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
}
