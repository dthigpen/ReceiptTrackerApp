package com.davidthigpen.receipttracker.ui.ReceiptList;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidthigpen.receipttracker.R;
import com.davidthigpen.receipttracker.BR;
import com.davidthigpen.receipttracker.data.model.Receipt;
import com.davidthigpen.receipttracker.databinding.RowSimpleBinding;

//import com.davidthigpen.receipttracker.databinding.RowSimpleBinding;

import java.util.List;

/**
 * Created by david on 1/14/18.
 */

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private List<Receipt> mReceipts;

    private ReceiptsHome.ReceiptClickListener mReceiptClickListener;


    public ReceiptAdapter(List<Receipt> receiptItems, ReceiptsHome.ReceiptClickListener receiptClickListener){
        setList(receiptItems);
        mReceiptClickListener = receiptClickListener;

    }

    public void setList(List<Receipt> items){
        mReceipts = items;
        Log.d("ReceiptAdapter","List size: " + items.size());
        notifyDataSetChanged();
    }
    public void addItem(Receipt receipt){
        mReceipts.add(receipt);
        notifyDataSetChanged();
    }
    public void addItems(List<Receipt> receipts){
        mReceipts.addAll(receipts);
        notifyDataSetChanged();
    }
    public void clearList(){
        mReceipts.clear();
        notifyDataSetChanged();
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowSimpleBinding binding = DataBindingUtil.inflate(layoutInflater,R.layout.row_simple,parent, false);

         return new ReceiptViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {
        Receipt item = mReceipts.get(position);

        ReceiptViewHolder receiptViewHolder = (ReceiptViewHolder) holder;
        receiptViewHolder.bindItem(item);

    }

    @Override
    public int getItemCount() {
        return mReceipts.size();
    }

    public class ReceiptViewHolder extends RecyclerView.ViewHolder{
            final RowSimpleBinding binding;
        public ReceiptViewHolder(final RowSimpleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mReceiptClickListener.onReceiptClicked(binding.getReceipt());
                }
            });
        }
        void bindItem(Receipt receipt){
            binding.setReceipt(receipt);
            binding.executePendingBindings();
        }
    }

}
