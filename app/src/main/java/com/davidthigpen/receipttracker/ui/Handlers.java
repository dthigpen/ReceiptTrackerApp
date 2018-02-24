package com.davidthigpen.receipttracker.ui;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.davidthigpen.receipttracker.databinding.ActivityEditItemBinding;

/**
 * Created by david on 2/23/18.
 */

public interface Handlers {
    //    private void incrementQuantity(View view)
//    {
//        ActivityEditItemBinding binding = DataBindingUtil.findBinding(view);
//        int oldVal = binding.getItem().getQuantity();
//        binding.getItem().setQuantity(++oldVal);
//    }
    void incrementQuantity(View view);

    //    private void decrementQuantity(View view){
//        ActivityEditItemBinding binding = DataBindingUtil.findBinding(view);
//        int oldVal = binding.getItem().getQuantity();
//        binding.getItem().setQuantity(oldVal <= 0 ? 0 : --oldVal);
//    }
    void decrementQuantity(View view);
}