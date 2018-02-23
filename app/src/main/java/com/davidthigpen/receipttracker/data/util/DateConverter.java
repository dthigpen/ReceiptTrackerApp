package com.davidthigpen.receipttracker.data.util;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by david on 2/8/18.
 */

public class DateConverter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}