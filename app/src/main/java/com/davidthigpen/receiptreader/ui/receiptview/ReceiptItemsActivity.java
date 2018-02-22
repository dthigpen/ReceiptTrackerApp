package com.davidthigpen.receiptreader.ui.receiptview;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.davidthigpen.receiptreader.R;
import com.davidthigpen.receiptreader.data.database.AppDatabase;
import com.davidthigpen.receiptreader.data.database.DatabaseHelper;
import com.davidthigpen.receiptreader.data.model.Receipt;
import com.davidthigpen.receiptreader.data.model.ReceiptItem;
import com.davidthigpen.receiptreader.data.util.DateFormatter;
import com.davidthigpen.receiptreader.ui.EditItem.EditItemActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.davidthigpen.receiptreader.ReceiptsHome.EXTRA_RECEIPT_ID;

public class ReceiptItemsActivity extends AppCompatActivity{

    private static final String TAG  = "ReceiptItemsActivity";
    public static final int EDIT_ITEM_REQUEST = 3;
    public static final String ITEM_POSITION_EXTRA =  "com.davidthigpen.receiptreader.ITEM_POSITION_EXTRA";
    public static final String ITEM_ID_EXTRA =  "com.davidthigpen.receiptreader.ITEM_ID_EXTRA";
    public static final String ITEM_NAME_EXTRA =  "com.davidthigpen.receiptreader.ITEM_NAME_EXTRA";
    public static final String ITEM_PRICE_EXTRA =  "com.davidthigpen.receiptreader.ITEM_PRICE_EXTRA";
    public static final String ITEM_QUANTITY_EXTRA =  "com.davidthigpen.receiptreader.ITEM_QUANTITY_EXTRA";
    public static final String ITEM_SPLITTER_IDS_EXTRA = "com.davidthigpen.receiptreader.ITEM_SPLITTER_IDS_EXTRA";

    TextView storeNameEdit;
    TextView receiptDateEdit;
    LinearLayoutManager mLinearLayoutManager;
    RecyclerView mItemsRecyclerView;
    Receipt mReceipt;
    List<ReceiptItem> mReceiptItems = new ArrayList<>();
    ReceiptItemAdapter mReceiptItemsAdapter;
    private DatePickerDialog mDatePickerDialog;
    private String mReceiptId;
    private DatabaseHelper mDatabaseHelper = new DatabaseHelper(AppDatabase.getInMemoryDatabase(this));

    private ReceiptItemClickListener mItemClickListener = new ReceiptItemClickListener() {
        @Override
        public void onItemClicked(int adapterPosition, ReceiptItem item) {
            showEditItemActivity(adapterPosition, item);
        }
    };


    private void showEditItemActivity(int adapterPosition, ReceiptItem item){
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(ITEM_POSITION_EXTRA,adapterPosition);
        intent.putExtra(ITEM_ID_EXTRA,item.getId());
        intent.putExtra(ITEM_NAME_EXTRA,item.getItemName());
        intent.putExtra(ITEM_PRICE_EXTRA,item.getPrice());
        intent.putExtra(ITEM_QUANTITY_EXTRA,item.getQuantity());
        intent.putStringArrayListExtra(ITEM_SPLITTER_IDS_EXTRA,item.getSplitterIds());
        startActivityForResult(intent,EDIT_ITEM_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_ITEM_REQUEST){
            if(resultCode == RESULT_OK){
                //TODO save list item data
                if(data != null) {
                    Log.d(TAG,data.getStringExtra(ITEM_NAME_EXTRA));
                }
                //TODO refresh item list
            }
        }
    }

    private void showEditItemDialog(ReceiptItem item){

//        Toast.makeText(this,item.getId() + ": " + item.getItemName(),Toast.LENGTH_SHORT).show();
//        EditItemFragment editItemFragment = new EditItemFragment();
//        editItemFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogFragmentTheme);
//        editItemFragment.show(getSupportFragmentManager(),"ItemFragment");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.edit_item_alert_dialog, null))
        // Add the buttons
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storeNameEdit = (TextView)findViewById(R.id.store_name_edit);
        mItemsRecyclerView = (RecyclerView)findViewById(R.id.receipt_items_list);
        receiptDateEdit = (TextView)findViewById(R.id.receipt_date_edit);
        receiptDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        Intent intent = getIntent();
        mReceiptId = intent.getStringExtra(EXTRA_RECEIPT_ID);
        if(mReceiptId != null){
            loadReceiptsData(mReceiptId);
        }else{
            Log.d(TAG,"Error no receipt id");
        }

        mLinearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mItemsRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mItemsRecyclerView.setLayoutManager(mLinearLayoutManager);
        mItemsRecyclerView.addItemDecoration(mDividerItemDecoration);

        mReceiptItemsAdapter  = new ReceiptItemAdapter(this,mReceiptItems, mItemClickListener);
        mItemsRecyclerView.setAdapter(mReceiptItemsAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                Calendar day = Calendar.getInstance();
                day.set(i,i1,i2);
                receiptDateEdit.setText(DateFormatter.dateToString(new Date(day.getTimeInMillis())));
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

    //TODO load all at once or break up
    public void loadReceiptsData(final String receiptId){
        new AsyncTask<Void,Void,Receipt>(){
            @Override
            protected Receipt doInBackground(Void... voids) {
                return mDatabaseHelper.getReceipt(receiptId,true);
            }

            @Override
            protected void onPostExecute(Receipt receipt) {
                if(receipt != null) {
                    mReceipt = receipt;
                    mReceiptItems = mReceipt.getReceiptItems();
                    mReceiptItemsAdapter.setList(receipt.getReceiptItems());
                    storeNameEdit.setText(mReceipt.getStore());
                    //TODO format date text
                    receiptDateEdit.setText(DateFormatter.dateToString(mReceipt.getReceiptDate()));
                }
            }
        }.execute();

    }

    public interface ReceiptItemClickListener{
        void onItemClicked(int adapterPosition, ReceiptItem item);
//        void onItemLongClicked(ReceiptItem item);
            //TODO handle long clicks
    }
}
