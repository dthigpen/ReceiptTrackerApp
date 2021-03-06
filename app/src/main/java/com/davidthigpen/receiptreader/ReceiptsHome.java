package com.davidthigpen.receiptreader;

import android.content.Intent;
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

import com.davidthigpen.receiptreader.data.database.DatabaseHelper;
import com.davidthigpen.receiptreader.data.database.AppDatabase;
import com.davidthigpen.receiptreader.data.model.Receipt;
import com.davidthigpen.receiptreader.ui.camera.CameraMode;
import com.davidthigpen.receiptreader.ui.receiptview.ReceiptItemsActivity;

import java.util.ArrayList;
import java.util.List;

public class ReceiptsHome extends AppCompatActivity {

    public static final String TAG = ReceiptsHome.class.getSimpleName();
    public static final String EXTRA_RECEIPT_ID = "com.davidthigpen.receiptreader.RECEIPT_ID";

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ReceiptAdapter mReceiptAdapter;
    private List<Receipt> mReceiptList = new ArrayList<>();
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
        setContentView(R.layout.activity_receipts_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseHelper  = new DatabaseHelper(AppDatabase.getInMemoryDatabase(this));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newReceiptByCamera();
            }
        });
        mRecyclerView = findViewById(R.id.receipt_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        //populateAndLoad with fake data then load
        populateAndLoad();
        mReceiptAdapter = new ReceiptAdapter(this,mReceiptList, mReceiptClickListener);
        mRecyclerView.setAdapter(mReceiptAdapter);
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
            mReceiptList = receipts;
            mReceiptAdapter.setList(receipts);
        }
    }
    public void viewReceiptDetail(Receipt receipt){
        Intent intent = new Intent(ReceiptsHome.this, ReceiptItemsActivity.class);
        intent.putExtra(EXTRA_RECEIPT_ID,receipt.getId());
        startActivity(intent);
    }
    public void newReceiptByCamera(){
        Intent intent = new Intent(this, CameraMode.class);
        startActivity(intent);
    }

    public interface ReceiptClickListener{
        void onReceiptClicked(Receipt receipt);
        //TODO make long click listener method
//        void onReceiptLongClicked();
    }

}
