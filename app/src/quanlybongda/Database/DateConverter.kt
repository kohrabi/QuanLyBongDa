package com.example.quanlybongda.Database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateConverter {
    @TypeConverter
    fun localDateTimeToString(dateTime : LocalDateTime?) : String {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateTime).toString();
    }

    @TypeConverter
    fun stringToLocalDateTime(dateTimeString : String) : LocalDateTime {
        return LocalDateTime.parse(dateTimeString);
    }

    @TypeConverter
    fun localDateToString(dateTime : LocalDate?) : String {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(dateTime).toString();
    }

    @TypeConverter
    fun stringToLocalDate(dateString : String) : LocalDate {
        return LocalDate.parse(dateString);
    }
}