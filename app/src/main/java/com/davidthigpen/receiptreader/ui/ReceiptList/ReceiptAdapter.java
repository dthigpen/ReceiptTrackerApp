package com.davidthigpen.receiptreader.ui.ReceiptList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.davidthigpen.receiptreader.R;
import com.davidthigpen.receiptreader.data.model.Receipt;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by david on 1/14/18.
 */

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {

    private List<Receipt> mReceipts;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ReceiptsHome.ReceiptClickListener mReceiptClickListener;

    public ReceiptAdapter(Context context, List<Receipt> receiptItems, ReceiptsHome.ReceiptClickListener receiptClickListener){
        mContext = context;
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

    public void clickedOnView(View view, int position){
        Log.d("ReceiptAdapter","Clicked");
        //TODO remove switch if action is indifferent per view
        switch (view.getId()){
            case R.id.title:
            case R.id.subtitle:
            case R.id.right_title:
                Toast.makeText(mContext,"Position " + position + " clicked.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         mLayoutInflater = LayoutInflater.from(mContext);
//         View receiptview = mLayoutInflater.inflate(android.R.layout.simple_list_item_2,parent,false);
        View receiptview = mLayoutInflater.inflate(R.layout.row_simple,parent,false);
         return new ViewHolder(receiptview);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Receipt item = mReceipts.get(position);

        //TODO use custom row item layout
        holder.storeNameText.setText(item.getStore());
        holder.receiptDateText.setText(new SimpleDateFormat("MM/dd/yyyy").format(item.getReceiptDate()));
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String dollarsString = formatter.format(item.getPriceTotal());
        holder.receiptTotalText.setText(dollarsString);

    }

    @Override
    public int getItemCount() {
        return mReceipts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView storeNameText;
        private TextView receiptDateText;
        private TextView receiptTotalText;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            storeNameText = itemView.findViewById(R.id.title);
            receiptDateText= itemView.findViewById(R.id.subtitle);
            receiptTotalText = itemView.findViewById(R.id.right_title);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mReceiptClickListener.onReceiptClicked(mReceipts.get(getAdapterPosition()));
                }
            });
        }
    }

}
