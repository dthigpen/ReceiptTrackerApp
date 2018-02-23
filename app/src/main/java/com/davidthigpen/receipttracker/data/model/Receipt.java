package com.davidthigpen.receipttracker.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.davidthigpen.receipttracker.data.util.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by david on 2/8/18.
 */

@Entity
@TypeConverters(DateConverter.class)
public class Receipt {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "store_name")
    private String store;
    @ColumnInfo(name = "entry_date")
    private Date entryDate;
    @ColumnInfo(name = "receipt_date")
    private Date receiptDate;
    @ColumnInfo(name = "price_subtotal")
    private double priceSubtotal;
    private double tax;

    @Ignore
    private List<ReceiptItem> mReceiptItems;

    @Ignore
    private List<Splitter> mSplitters;

    public Receipt(){
        id = UUID.randomUUID().toString();
        mReceiptItems = new ArrayList<>();
    }


    //TODO add quantity
//    public void addNewItem(String itemName, double price, List<Splitter> splitters){
//        ReceiptItem receiptItem = new ReceiptItem();
//        receiptItem.setReceiptId(id);
//        receiptItem.setItemName(itemName);
//        receiptItem.setPrice(price);
//        receiptItem.setSplitters(splitters);
//        //TODO add new splitters to receipts list of splitters
//        //TODO keep items up to date, possibly adapt setter method
//        mReceiptItems.add(receiptItem);
//        mReceiptItems.add(receiptItem);
//    }

    //getters and setters
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public double getPriceSubtotal() {
        return priceSubtotal;
    }

    public void setPriceSubtotal(double priceSubtotal) {
        this.priceSubtotal = priceSubtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public List<ReceiptItem> getReceiptItems() {
        return mReceiptItems;
    }

    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        mReceiptItems = receiptItems;
    }

    public List<Splitter> getSplitters() {
        return mSplitters;
    }

    public void setSplitters(List<Splitter> splitters) {
        mSplitters = splitters;
    }
}
