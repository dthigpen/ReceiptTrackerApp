package com.davidthigpen.receiptreader.ui.ReceiptDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davidthigpen.receiptreader.R;
import com.davidthigpen.receiptreader.data.model.ReceiptItem;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by david on 1/14/18.
 */

public class ReceiptItemAdapter extends RecyclerView.Adapter<ReceiptItemAdapter.ViewHolder> {

    private List<ReceiptItem> mReceiptItems;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ReceiptItemsActivity.ReceiptItemClickListener mReceiptClickListener;

    public ReceiptItemAdapter(Context context, List<ReceiptItem> receiptItems, ReceiptItemsActivity.ReceiptItemClickListener receiptItemClickListener){
        mContext = context;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         mLayoutInflater = LayoutInflater.from(mContext);
        View receiptItemView = mLayoutInflater.inflate(R.layout.row_receipt_item,parent,false);
         return new ViewHolder(receiptItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       ReceiptItem item = mReceiptItems.get(position);

        holder.itemNameText.setText(item.getItemName() != null ? item.getItemName() : ("Id: " + item.getId()));
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String dollarsString = formatter.format(item.getPrice());
        holder.itemDollarAmount.setText(dollarsString);

    }

    @Override
    public int getItemCount() {
        if(mReceiptItems != null)
            return mReceiptItems.size();
        else return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView itemNameText;
        private TextView itemDollarAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemNameText = itemView.findViewById(R.id.item_name);
            itemDollarAmount = itemView.findViewById(R.id.item_price);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mReceiptClickListener.onItemClicked(getAdapterPosition(),mReceiptItems.get(getAdapterPosition()));
                }
            });
        }
    }

}
