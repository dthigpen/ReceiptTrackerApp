package com.davidthigpen.receipttracker.ui.ReceiptList;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.davidthigpen.receipttracker.R;
import com.davidthigpen.receipttracker.data.database.DatabaseHelper;
import com.davidthigpen.receipttracker.data.database.AppDatabase;
import com.davidthigpen.receipttracker.data.model.Receipt;

import com.davidthigpen.receipttracker.databinding.ActivityReceiptsHomeBinding;

import com.davidthigpen.receipttracker.ui.ReceiptDetail.ReceiptItemsActivity;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsHome extends AppCompatActivity {

    public static final String TAG = ReceiptsHome.class.getSimpleName();
    public static final String EXTRA_RECEIPT_ID = "com.davidthigpen.receiptreader.RECEIPT_ID";

    private ActivityReceiptsHomeBinding binding;
    private DatabaseHelper mDatabaseHelper;

    private ReceiptClickListener mReceiptClickListener = new ReceiptClickListener() {
        @Override
        public void onReceiptClicked(Receipt receipt) {
            Toast.makeText(ReceiptsHome.this, "Receipt Id: " + receipt.getId(), Toast.LENGTH_SHORT).show();
            viewReceiptDetail(receipt);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_receipts_home);
        binding.receiptList.setLayoutManager(new LinearLayoutManager(this));
        setSupportActionBar(binding.toolbar);

        mDatabaseHelper  = new DatabaseHelper(AppDatabase.getInMemoryDatabase(this));

        populateAndLoad();
    }

    private void displayItems(List<Receipt> items){
        Log.d(TAG,"displayItems: " + items.size());
        binding.receiptList.setAdapter(new ReceiptAdapter(items, mReceiptClickListener));
        binding.receiptList.setLayoutManager(new LinearLayoutManager(this));

    }
    private void loadReceiptData(){
        FetchReceiptData task = new FetchReceiptData();
        task.execute();
    }
    private void populateAndLoad(){
        PopulateDatabaseTemp task = new PopulateDatabaseTemp();
        task.execute();
    }
    private class PopulateDatabaseTemp extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            mDatabaseHelper.populateWithTestData(mDatabaseHelper.getAppDatabase());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadReceiptData();
        }
    }
    private class FetchReceiptData extends AsyncTask<Void,Void,List<Receipt>>{
        @Override
        protected List<Receipt> doInBackground(Void... voids) {
            return mDatabaseHelper.getReceipts(false);
        }

        @Override
        protected void onPostExecute(List<Receipt> receipts) {
            Log.d("ReceiptsHome","Receipts Size: " + receipts.size());
            displayItems(receipts);

        }
    }
    public void viewReceiptDetail(Receipt receipt){
        Intent intent = new Intent(ReceiptsHome.this, ReceiptItemsActivity.class);
        intent.putExtra(EXTRA_RECEIPT_ID,receipt.getId());
        startActivity(intent);
    }

    public interface ReceiptClickListener{
        void onReceiptClicked(Receipt receipt);
        //TODO make long click listener method
//        void onReceiptLongClicked();
    }

}
