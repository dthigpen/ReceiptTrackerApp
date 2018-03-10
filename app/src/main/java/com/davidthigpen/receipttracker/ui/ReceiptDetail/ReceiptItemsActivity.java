package com.davidthigpen.receipttracker.ui.ReceiptDetail;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.davidthigpen.receipttracker.R;
import com.davidthigpen.receipttracker.data.database.AppDatabase;
import com.davidthigpen.receipttracker.data.database.DatabaseHelper;
import com.davidthigpen.receipttracker.data.model.Receipt;
import com.davidthigpen.receipttracker.data.model.ReceiptItem;
import com.davidthigpen.receipttracker.data.util.DateFormatter;
import com.davidthigpen.receipttracker.databinding.ActivityReceiptItemsBinding;
import com.davidthigpen.receipttracker.databinding.ActivityReceiptsHomeBinding;
import com.davidthigpen.receipttracker.ui.EditItem.EditItemActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.davidthigpen.receipttracker.ui.ReceiptList.ReceiptsHome.EXTRA_RECEIPT_ID;

public class ReceiptItemsActivity extends AppCompatActivity{

    private static final String TAG  = "ReceiptItemsActivity";
    public static final int EDIT_ITEM_REQUEST = 3;
    public static final String ITEM_POSITION_EXTRA =  "com.davidthigpen.receiptreader.ITEM_POSITION_EXTRA";
    public static final String RECEIPT_ID_EXTRA =  "com.davidthigpen.receiptreader.RECEIPT_ID_EXTRA";
    public static final String ITEM_ID_EXTRA =  "com.davidthigpen.receiptreader.ITEM_ID_EXTRA";
    public static final String ITEM_NAME_EXTRA =  "com.davidthigpen.receiptreader.ITEM_NAME_EXTRA";
    public static final String ITEM_PRICE_EXTRA =  "com.davidthigpen.receiptreader.ITEM_PRICE_EXTRA";
    public static final String ITEM_QUANTITY_EXTRA =  "com.davidthigpen.receiptreader.ITEM_QUANTITY_EXTRA";
    public static final String ITEM_SPLITTER_IDS_EXTRA = "com.davidthigpen.receiptreader.ITEM_SPLITTER_IDS_EXTRA";

    private ActivityReceiptItemsBinding binding;
    List<ReceiptItem> mReceiptItems = new ArrayList<>();
    ReceiptItemAdapter mReceiptItemsAdapter;
    private DatePickerDialog mDatePickerDialog;
    private String mReceiptId;
    private DatabaseHelper mDatabaseHelper = new DatabaseHelper(AppDatabase.getInMemoryDatabase(this));
    private int editItemPosition;

    private ReceiptItemClickListener mItemClickListener = new ReceiptItemClickListener() {
        @Override
        public void onItemClicked(ReceiptItem item) {
            showEditItemActivity(item);
        }
    };


    private void showEditItemActivity(ReceiptItem item){
        Intent intent = new Intent(this, EditItemActivity.class);
//        intent.putExtra(RECEIPT_ID_EXTRA,item.getReceiptId());
        intent.putExtra(ITEM_ID_EXTRA,item.getId());
//        intent.putExtra(ITEM_NAME_EXTRA,item.getItemName());
//        intent.putExtra(ITEM_PRICE_EXTRA,item.getPrice());
//        intent.putExtra(ITEM_QUANTITY_EXTRA,item.getQuantity());
//        intent.putStringArrayListExtra(ITEM_SPLITTER_IDS_EXTRA,item.getSplitterIds());
        startActivityForResult(intent,EDIT_ITEM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_ITEM_REQUEST){
            if(resultCode == RESULT_OK){
                loadReceiptItemData(mReceiptId);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_receipt_items);
        setSupportActionBar(binding.toolbar);

        Intent intent = getIntent();
        mReceiptId = intent.getStringExtra(EXTRA_RECEIPT_ID);
        if(mReceiptId != null){
            loadReceiptsData(mReceiptId);
            loadReceiptItemData(mReceiptId);
        }else{
            Log.d(TAG,"Error no receipt id");
        }
        binding.receiptDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        binding.receiptItemsList.setLayoutManager(new LinearLayoutManager(this));
        mReceiptItemsAdapter  = new ReceiptItemAdapter(mReceiptItems, mItemClickListener);
        binding.receiptItemsList.setAdapter(mReceiptItemsAdapter);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        editItemPosition = 0;
    }

    private void showDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                Calendar day = Calendar.getInstance();
                day.set(i,i1,i2);
                binding.receiptDateEdit.setText(DateFormatter.dateToString(new Date(day.getTimeInMillis())));
            }
        };
        Calendar calendarNow = Calendar.getInstance();

        mDatePickerDialog = new DatePickerDialog(
                this,dateSetListener,
                calendarNow.get(Calendar.YEAR),
                calendarNow.get(Calendar.MONTH),
                calendarNow.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();
    }

    public void loadReceiptItemData(final String receiptId){
        new AsyncTask<Void,Void,List<ReceiptItem>>(){
            @Override
            protected List<ReceiptItem> doInBackground(Void... voids) {
                return mDatabaseHelper.getAppDatabase().getReceiptItemDao().loadAllByReceiptId(receiptId);
            }

            @Override
            protected void onPostExecute(List<ReceiptItem> items) {
                if(items != null) {
                    mReceiptItems = items;
                    mReceiptItemsAdapter.setList(items);
                }
            }
        }.execute();

    }
    public void loadReceiptsData(final String receiptId){
        new AsyncTask<Void,Void,Receipt>(){
            @Override
            protected Receipt doInBackground(Void... voids) {
                return mDatabaseHelper.getReceipt(receiptId,true);
            }

            @Override
            protected void onPostExecute(Receipt receipt) {
                if(receipt != null) {
                    binding.setReceipt(receipt);
//                    mReceiptItems = receipt.getReceiptItems();
//                    mReceiptItemsAdapter.setList(receipt.getReceiptItems());
                }
            }
        }.execute();

    }

    public interface ReceiptItemClickListener{
        void onItemClicked(ReceiptItem item);
//        void onItemLongClicked(ReceiptItem item);
            //TODO handle long clicks
    }
}
