package com.example.fit5046_assignment2;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverters {

    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }
    @TypeConverter
    public Date convertLongToDate(long time) {
        return new Date(time);
    }
}
