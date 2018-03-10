package com.davidthigpen.receipttracker.ui.ReceiptDetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidthigpen.receipttracker.R;
import com.davidthigpen.receipttracker.data.model.ReceiptItem;
import com.davidthigpen.receipttracker.databinding.RowReceiptItemBinding;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by david on 1/14/18.
 */

public class ReceiptItemAdapter extends RecyclerView.Adapter<ReceiptItemAdapter.ItemViewHolder> {

    private List<ReceiptItem> mReceiptItems;
    private LayoutInflater mLayoutInflater;
    private ReceiptItemsActivity.ReceiptItemClickListener mReceiptClickListener;

    public ReceiptItemAdapter( List<ReceiptItem> receiptItems, ReceiptItemsActivity.ReceiptItemClickListener receiptItemClickListener){
        setList(receiptItems);
        mReceiptClickListener = receiptItemClickListener;

    }

    public void setList(List<ReceiptItem> items){
        mReceiptItems = items;
        notifyDataSetChanged();
    }
    public void addItem(ReceiptItem receiptItem){
        mReceiptItems.add(receiptItem);
        notifyDataSetChanged();
    }
    public void addItems(List<ReceiptItem> receiptItems){
        mReceiptItems.addAll(receiptItems);
        notifyDataSetChanged();
    }
    public void clearList(){
        mReceiptItems.clear();
        notifyDataSetChanged();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         mLayoutInflater = LayoutInflater.from(parent.getContext());
        RowReceiptItemBinding binding = DataBindingUtil.inflate(mLayoutInflater,R.layout.row_receipt_item,parent,false);
         return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
       ReceiptItem item = mReceiptItems.get(position);

        holder.bindItem(item);

    }

    @Override
    public int getItemCount() {
        if(mReceiptItems != null)
            return mReceiptItems.size();
        else return 0;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{
        final RowReceiptItemBinding binding;

        public ItemViewHolder(final RowReceiptItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mReceiptClickListener.onItemClicked(binding.getItem());
                }
            });
        }
        void bindItem(ReceiptItem item){
            binding.setItem(item);
            binding.executePendingBindings();
        }
    }

}
