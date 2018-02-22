package com.davidthigpen.receiptreader.data.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david on 2/19/18.
 */

public class DateFormatter {
    public static final String DATE_FORMAT = "M-dd-YYYY";
    public static Date stringToDate(String dateString){
        if(dateString ==null || dateString.trim().equals("")){
            return null;
        }
        try{
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            return format.parse(dateString);
        }catch (ParseException e){
            e.printStackTrace();
            Log.d("DateFormatter","Parse Exception, using Null date instead.");
        }
        return null;
    }

    public static String dateToString(Date date){
        if(date == null){
            return " ";
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }
}
