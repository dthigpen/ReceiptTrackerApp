package com.davidthigpen.receiptreader.ui.EditItem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidthigpen.receiptreader.R;
import com.davidthigpen.receiptreader.ui.ReceiptDetail.ReceiptItemsActivity;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {

    private EditText itemNameEdit;
    private EditText itemPriceEdit;
    private TextView quantityText;
    private ImageView quantitySub;
    private ImageView quantityAdd;

    private String itemNameString;
    private double itemPrice;
    private int quantity;
    private ArrayList<String> splitterIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemNameEdit = findViewById(R.id.item_name_edit);
        itemPriceEdit = findViewById(R.id.item_price_edit);
        quantityText = findViewById(R.id.quantity);
        quantityAdd = findViewById(R.id.quantity_add);
        quantitySub = findViewById(R.id.quantity_subtract);

        Intent intent = getIntent();
        itemNameString = intent.getStringExtra(ReceiptItemsActivity.ITEM_NAME_EXTRA);
        itemPrice = intent.getDoubleExtra(ReceiptItemsActivity.ITEM_PRICE_EXTRA,0);
        quantity = intent.getIntExtra(ReceiptItemsActivity.ITEM_QUANTITY_EXTRA,1);
        splitterIds = intent.getStringArrayListExtra(ReceiptItemsActivity.ITEM_SPLITTER_IDS_EXTRA);

        itemNameEdit.setText(itemNameString);
        itemPriceEdit.setText("" + itemPrice);
        quantityText.setText("" + quantity);

    }

    public void onQuantityChanged(View view){
        switch (view.getId()){
            case R.id.quantity_add:
                quantity = quantity <= 0 ? 0 : quantity + 1;
                quantityText.setText("" + quantity);
                break;
            case R.id.quantity_subtract:
                quantity = quantity <= 0 ? 0 : quantity - 1;
                quantityText.setText("" + quantity);
                break;
        }
    }

    private void packValues(){
        Intent intent = new Intent();
        intent.putExtra(ReceiptItemsActivity.ITEM_NAME_EXTRA,itemNameEdit.getText().toString());
        //TODO catch parse error
        intent.putExtra(ReceiptItemsActivity.ITEM_PRICE_EXTRA,Double.parseDouble(itemPriceEdit.getText().toString()));
        intent.putExtra(ReceiptItemsActivity.ITEM_QUANTITY_EXTRA,Integer.parseInt(quantityText.getText().toString()));
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
