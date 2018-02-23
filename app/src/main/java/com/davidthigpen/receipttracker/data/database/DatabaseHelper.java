package com.davidthigpen.receipttracker.data.database;


import android.os.AsyncTask;
import android.util.Log;

import com.davidthigpen.receipttracker.data.model.Receipt;
import com.davidthigpen.receipttracker.data.model.ReceiptItem;
import com.davidthigpen.receipttracker.data.model.ReceiptItemSplitterJoin;
import com.davidthigpen.receipttracker.data.model.Splitter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 2/13/18.
 */

public class DatabaseHelper {

    private AppDatabase mAppDatabase;

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

    public DatabaseHelper(AppDatabase appDatabase){
        mAppDatabase = appDatabase;
    }

    public List<ReceiptItem> getReceiptItems(){
        List<ReceiptItem> receiptItems = mAppDatabase.getReceiptItemDao().getAllItems();
        for(ReceiptItem item : receiptItems){
            item.setSplitters(mAppDatabase.getItemSplitterJoinDao().getSplittersForReceiptItem(item.getId()));
        }
        return receiptItems;
    }

    public List<Receipt> getReceipts(boolean fillWithItemsAndSplitters){
        //TODO add relations to database to make it more efficient, dis bad
        List<Receipt> receipts = mAppDatabase.getReceiptDao().getAllReceipts();
        if(fillWithItemsAndSplitters){
            List<Splitter> splitters = new ArrayList<>();
            for(Receipt receipt : receipts){
                //for each receipt attach list of items and splitters
                receipt.setReceiptItems(mAppDatabase.getReceiptItemDao().loadAllByReceiptId(receipt.getId()));
                for(ReceiptItem item : receipt.getReceiptItems()){
                    List<Splitter> itemSplitters = mAppDatabase.getItemSplitterJoinDao().getSplittersForReceiptItem(item.getId());
                    item.setSplitters(itemSplitters);
                    for(Splitter splitter : itemSplitters){
                        if(!splitters.contains(splitter)){
                            splitters.add(splitter);
                        }
                    }
                }

            }
        }
        return receipts;
    }
    public Receipt getReceipt(String id, boolean fillWithItemsAndSplitters){
        //TODO add relations to database to make it more efficient, dis bad
        Receipt receipt = mAppDatabase.getReceiptDao().loadById(id);
        List<Splitter> splitters = new ArrayList<>();
        if(fillWithItemsAndSplitters){
                //for each receipt attach list of items and splitters
                receipt.setReceiptItems(mAppDatabase.getReceiptItemDao().loadAllByReceiptId(receipt.getId()));
                for(ReceiptItem item : receipt.getReceiptItems()){
                    List<Splitter> itemSplitters = mAppDatabase.getItemSplitterJoinDao().getSplittersForReceiptItem(item.getId());
                    item.setSplitters(itemSplitters);
                    for(Splitter splitter : itemSplitters){
                        if(!splitters.contains(splitter)){
                            splitters.add(splitter);
                        }
                    }
                }
        }
        return receipt;
    }
    public static void populateAsync(final AppDatabase db) {
        DatabaseHelper.PopulateDbAsync task = new DatabaseHelper.PopulateDbAsync(db);
        task.execute();
    }
    public void addReceiptAsync(Receipt receipt){
        AddReceiptAsync task = new AddReceiptAsync(mAppDatabase);
        task.execute(receipt);
    }
    public static void addReceipt(final AppDatabase db, Receipt receipt){
        db.getReceiptDao().insertReceipt(receipt);

        //It is assumed that all items are not linked to their receipt
        db.getReceiptDao().insertReceipt(receipt);
        for(ReceiptItem item : receipt.getReceiptItems()){
            item.setReceiptId(receipt.getId());
            for(Splitter splitter : item.getSplitters()){
                //saving the relationship between receipt Items and splitters
                db.getItemSplitterJoinDao().insert(new ReceiptItemSplitterJoin(item.getId(),splitter.getId()));
            }
        }
        db.getReceiptItemDao().insert(receipt.getReceiptItems());

    }
    public static void addReceiptItem(final AppDatabase db, ReceiptItem item){
        if(item.getReceiptId() == null){
            Log.d("DatabaseHelper","ReceiptItem not attached to receipt");
            return;
        }
        for(Splitter splitter : item.getSplitters()){
            //TODO do something to ensure that it has already been done
            //saving the relationship between receipt Items and splitters
            db.getItemSplitterJoinDao().insert(new ReceiptItemSplitterJoin(item.getId(),splitter.getId()));
        }
        db.getReceiptItemDao().insert(item);
    }


    public static Receipt addReceiptFromData(final AppDatabase db, String storeName, Date receiptDate, Date entryDate,
                                     List<ReceiptItem> receiptItems, List<Splitter> splitters){
        Receipt receipt = new Receipt();
        receipt.setStore(storeName);
        receipt.setReceiptDate(receiptDate);
        receipt.setEntryDate(entryDate);
        receipt.setReceiptItems(receiptItems);
        receipt.setSplitters(splitters);
        db.getReceiptDao().insertReceipt(receipt);

        //TODO Link ids for items and splitters here or before
        db.getReceiptDao().insertReceipt(receipt);
        for(ReceiptItem item : receiptItems){
            item.setReceiptId(receipt.getId());
            for(Splitter splitter : item.getSplitters()){
                //saving the relationship between receipt Items and splitters
                db.getItemSplitterJoinDao().insert(new ReceiptItemSplitterJoin(item.getId(),splitter.getId()));
            }
        }
        db.getReceiptItemDao().insert(receiptItems);
        return receipt;
    }
    public static Splitter addSplitter(final AppDatabase db, String name){
        Splitter splitter = new Splitter();
        splitter.setName(name);
        db.getSplitterDao().insert(splitter);
        return splitter;
    }

    public static ReceiptItem addReceiptItem(final AppDatabase db, String itemName, double price){
        ReceiptItem receiptItem = new ReceiptItem();
        receiptItem.setItemName(itemName);
        receiptItem.setPrice(price);
        db.getReceiptItemDao().insert(receiptItem);
        return receiptItem;

    }


    public static void populateWithTestData(AppDatabase db){
        db.getReceiptDao().deleteAll();
        db.getReceiptItemDao().deleteAll();
        db.getSplitterDao().deleteAll();

        try {

            Splitter splitter1 = new Splitter();
            splitter1.setName("David");
            Splitter splitter2 = new Splitter();
            splitter2.setName("Bob");
            Splitter splitter3 = new Splitter();
            splitter3.setName("John");
            List<Splitter> splitters = new ArrayList<>();
            splitters.add(splitter1);
            splitters.add(splitter2);
            splitters.add(splitter3);

            for (int i = 1; i <= 3; i++) {
                Receipt receipt = new Receipt();
                receipt.setStore("Store " + i);
                receipt.setEntryDate(new Date(System.currentTimeMillis()));
                receipt.setReceiptDate(new Date(System.currentTimeMillis()));
                receipt.setPriceTotal(i*10.00);
                db.getReceiptDao().insertReceipt(receipt);
                Log.d("DatabaseHelper", "Poplulate, new receipt id: " + receipt.getId());
                for (int j = 1; j <= 4; j++) {
                    ReceiptItem item = new ReceiptItem();
                    item.setReceiptId(receipt.getId());
                    item.setItemName("Item " + j);
                    item.setPrice(j + 0.50);

                    db.getReceiptItemDao().insert(item);
                    //selects a sublist of the splitters to split the item ranging from all of the 0,1,2 to none of them
                    //TODO fix foreign constraint error on splitter relationship
                    if (false && (j - 1) < splitters.size()) {
                        item.setSplitters(splitters.subList(j, splitters.size()));
                        for (Splitter splitter : item.getSplitters()) {
                            db.getItemSplitterJoinDao().insert(new ReceiptItemSplitterJoin(item.getId(), splitter.getId()));
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.d("DatabaseHelper", "Error populating with test data");
            e.printStackTrace();
        }

        Log.d("DatabaseHelper", "Populated Receipts Size: " + db.getReceiptDao().getAllReceipts().size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {
        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db){
            mDb = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            populateWithTestData(mDb);
            return null;
        }
    }
    private static class AddReceiptAsync extends AsyncTask<Receipt,Void,Void> {
        private final AppDatabase mDb;

        AddReceiptAsync(AppDatabase db){
            mDb = db;
        }

        @Override
        protected Void doInBackground(Receipt... receipts) {
            addReceipt(mDb,receipts[0]);
            return null;
        }
    }
    private static class AddReceiptItemAsync extends AsyncTask<ReceiptItem,Void,Void> {
        private final AppDatabase mDb;

        AddReceiptItemAsync(AppDatabase db){
            mDb = db;
        }

        @Override
        protected Void doInBackground(ReceiptItem... receiptItems) {
            addReceiptItem(mDb,receiptItems[0]);
            return null;
        }
    }
}

