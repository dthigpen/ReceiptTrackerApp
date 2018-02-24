package com.davidthigpen.receipttracker.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by david on 2/8/18.
 */
@Entity
public class ReceiptItem {

    @PrimaryKey
    @NonNull
    private String id;

    @ForeignKey(entity = Receipt.class,parentColumns = "id",childColumns = "receipt_id", onDelete = CASCADE)
    @NonNull
    private String receiptId;

    @ColumnInfo(name = "item_name")
    private String itemName;
    private double price;
    private int quantity;
    @Ignore
    private List<Splitter> splitters;

    @Ignore
    public ArrayList<String> getSplitterIds(){
        ArrayList<String> splitterIds = new ArrayList<>();
        for(Splitter splitter : splitters){
            splitterIds.add(splitter.getId());
        }
        return splitterIds;
    }
    public ReceiptItem(){
        id = UUID.randomUUID().toString();
    }

    @Ignore
    public ReceiptItem(String id){
        this.id = id;
    }
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    //getters and setters
    @NonNull
    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(@NonNull String receiptId) {
        this.receiptId = receiptId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Splitter> getSplitters() {
        return splitters;
    }

    public void setSplitters(List<Splitter> splitters) {
        this.splitters = splitters;
    }
}
