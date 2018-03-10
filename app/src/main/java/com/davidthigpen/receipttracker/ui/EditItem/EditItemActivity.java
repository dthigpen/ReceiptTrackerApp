package com.davidthigpen.receipttracker.ui.EditItem;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.davidthigpen.receipttracker.R;
import com.davidthigpen.receipttracker.data.database.AppDatabase;
import com.davidthigpen.receipttracker.data.database.DatabaseHelper;
import com.davidthigpen.receipttracker.data.model.ReceiptItem;
import com.davidthigpen.receipttracker.databinding.ActivityEditItemBinding;
import com.davidthigpen.receipttracker.ui.Handlers;
import com.davidthigpen.receipttracker.ui.ReceiptDetail.ReceiptItemsActivity;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity implements Handlers{

    private ActivityEditItemBinding binding;
    private ReceiptItem item;
    private String itemNameString;
    private String itemReceiptId;
    private double itemPrice;
    private int quantityNum;
    private ArrayList<String> splitterIds;
    private DatabaseHelper mDatabaseHelper = new DatabaseHelper(AppDatabase.getInMemoryDatabase(this));
    private boolean isNewItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
//        itemReceiptId = intent.getStringExtra(ReceiptItemsActivity.RECEIPT_ID_EXTRA);
        String itemId = intent.getStringExtra(ReceiptItemsActivity.ITEM_ID_EXTRA);
//        if(itemId != null){
//            itemNameString = intent.getStringExtra(ReceiptItemsActivity.ITEM_NAME_EXTRA);
//            itemPrice = intent.getDoubleExtra(ReceiptItemsActivity.ITEM_PRICE_EXTRA,0);
//            quantityNum = intent.getIntExtra(ReceiptItemsActivity.ITEM_QUANTITY_EXTRA,1);
//            splitterIds = intent.getStringArrayListExtra(ReceiptItemsActivity.ITEM_SPLITTER_IDS_EXTRA);
//             item = new ReceiptItem(itemId);
//             item.setReceiptId(itemReceiptId);
//             item.setItemName(itemNameString);
//             item.setPrice(itemPrice);
//             item.setQuantity(quantityNum);
//             binding.setItem(item);
//
//        }
        if(itemId != null) {

            isNewItem = false;
            loadItem(itemId);

        }else{
            isNewItem = true;
            binding.setItem(new ReceiptItem());
        }

        binding.setHandler(this);
    }

    @Override
    public void incrementQuantity(View view) {
        ReceiptItem item = binding.getItem();
        int quantity = item.getQuantity();
        item.setQuantity(++quantity);

    }

    @Override
    public void decrementQuantity(View view) {
        int quantity = binding.getItem().getQuantity();

        if(quantity > 0){
            ReceiptItem item = binding.getItem();
            item.setQuantity(--quantity);

        }
    }

    private void packValues(){
        Intent intent = new Intent();
        intent.putExtra(ReceiptItemsActivity.ITEM_NAME_EXTRA,binding.itemNameEdit.getText().toString());
        //TODO catch parse error
        intent.putExtra(ReceiptItemsActivity.ITEM_PRICE_EXTRA,Double.parseDouble(binding.itemPriceEdit.getText().toString()));
        intent.putExtra(ReceiptItemsActivity.ITEM_QUANTITY_EXTRA,Integer.parseInt(binding.quantity.getText().toString()));
        //TODO write splitters logic
        intent.putStringArrayListExtra(ReceiptItemsActivity.ITEM_SPLITTER_IDS_EXTRA,splitterIds);
        setResult(RESULT_OK,intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                packValues();
                setResult(RESULT_OK);
                saveItem();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
    private void loadItem(String itemId){
        AsyncTask<String,Void,ReceiptItem> task = new AsyncTask<String, Void, ReceiptItem>() {
            @Override
            protected ReceiptItem doInBackground(String... itemIds) {
                return mDatabaseHelper.getAppDatabase().getReceiptItemDao().loadItemById(itemIds[0]);
            }

            @Override
            protected void onPostExecute(ReceiptItem item)
            {
                binding.setItem(item);
            }
        };
        task.execute(itemId);
    }
    private void saveItem(){
        AsyncTask<Void,Void,Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if(isNewItem){
                    mDatabaseHelper.getAppDatabase().getReceiptItemDao().insert(binding.getItem());
                }else {
                    Log.d("EditItem","Item name: " + binding.getItem().getItemName());
                    Log.d("EditItem","Item price: " + binding.getItem().getPrice());
                    mDatabaseHelper.getAppDatabase().getReceiptItemDao().update(binding.getItem());
                }
                return null;
            }
        };
        task.execute();
    }
}
