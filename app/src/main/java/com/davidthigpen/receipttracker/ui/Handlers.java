package com.davidthigpen.receipttracker.ui;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.davidthigpen.receipttracker.databinding.ActivityEditItemBinding;

/**
 * Created by david on 2/23/18.
 */

public interface Handlers {

    void incrementQuantity(View view);
    void decrementQuantity(View view);
}