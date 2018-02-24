package com.davidthigpen.receipttracker.ui.EditItem;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.davidthigpen.receipttracker.BR;
import com.davidthigpen.receipttracker.R;
import com.davidthigpen.receipttracker.data.model.Receipt;
import com.davidthigpen.receipttracker.data.model.ReceiptItem;
import com.davidthigpen.receipttracker.databinding.ActivityEditItemBinding;
//import com.davidthigpen.receipttracker.ui.Handlers;
import com.davidthigpen.receipttracker.ui.Handlers;
import com.davidthigpen.receipttracker.ui.ReceiptDetail.ReceiptItemsActivity;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity implements Handlers{

    private ActivityEditItemBinding binding;
    private ReceiptItem item;
    private String itemNameString;
    private double itemPrice;
    private int quantityNum;
    private ArrayList<String> splitterIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String itemId = intent.getStringExtra(ReceiptItemsActivity.ITEM_ID_EXTRA);
        if(itemId != null){
            itemNameString = intent.getStringExtra(ReceiptItemsActivity.ITEM_NAME_EXTRA);
            itemPrice = intent.getDoubleExtra(ReceiptItemsActivity.ITEM_PRICE_EXTRA,0);
            quantityNum = intent.getIntExtra(ReceiptItemsActivity.ITEM_QUANTITY_EXTRA,1);
            splitterIds = intent.getStringArrayListExtra(ReceiptItemsActivity.ITEM_SPLITTER_IDS_EXTRA);
             item = new ReceiptItem(itemId);
             item.setItemName(itemNameString);
             item.setPrice(itemPrice);
             item.setQuantity(quantityNum);
             binding.setItem(item);
        }
        binding.setHandler(this);



//        Toast.makeText(this,itemNameString,Toast.LENGTH_SHORT).show();
//        binding.itemNameEdit.setText(itemNameString);
//        binding.itemPriceEdit.setText(String.format("%.2f",itemPrice));
//        binding.quantity.setText(String.format("" + quantityNum));



    }

    @Override
    public void incrementQuantity(View view) {
        ReceiptItem item = binding.getItem();
        int quantity =item.getQuantity();
        Log.d("Edit item","" + quantity);
        item.setQuantity(++quantity);
        binding.setItem(item);
        binding.notifyPropertyChanged(BR.item);
    }

    @Override
    public void decrementQuantity(View view) {
        int quantity = binding.getItem().getQuantity();

        if(quantity > 0){
            ReceiptItem item = binding.getItem();
            item.setQuantity(--quantity);
            binding.setItem(item);
            binding.notifyPropertyChanged(BR.item);
        }
    }

    private void onQuantityChanged(View view){
        switch (view.getId()){
            case R.id.quantity_add:
                quantityNum = quantityNum <= 0 ? 0 : quantityNum + 1;
                binding.quantity.setText("" + quantityNum);
                break;
            case R.id.quantity_subtract:
                quantityNum = quantityNum <= 0 ? 0 : quantityNum - 1;
                binding.quantity.setText("" + quantityNum);
                break;
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
                packValues();
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
