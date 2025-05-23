package com.example.quanlybongda.Database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

class DateConverter {
    companion object {

        fun LocalDateTimeToString(dateTime : LocalDateTime?) : String {
            if (dateTime == null)
                return LocalDateTime.now().atOffset(UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return dateTime.atOffset(UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        }

        fun StringToLocalDateTime(dateTimeString : String) : LocalDateTime {
            val offsetDateTime = OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            // Convert to LocalDateTime (drops the time zone)
            return offsetDateTime.toLocalDateTime()
        }

        fun LocalDateToString(dateTime : LocalDate?) : String {
            return DateTimeFormatter.ISO_LOCAL_DATE.format(dateTime).toString();
        }

        fun StringToLocalDate(dateString : String) : LocalDate {
            return LocalDate.parse(dateString);
        }
    }

    @TypeConverter
    fun localDateTimeToString(dateTime : LocalDateTime?) : String {
        return LocalDateTimeToString(dateTime);
    }

    @TypeConverter
    fun stringToLocalDateTime(dateTimeString : String) : LocalDateTime {
        return StringToLocalDateTime(dateTimeString);
    }

    @TypeConverter
    fun localDateToString(dateTime : LocalDate?) : String {
        return LocalDateToString(dateTime);
    }

    @TypeConverter
    fun stringToLocalDate(dateString : String) : LocalDate {
        return StringToLocalDate(dateString);
    }
}