package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Date? {
        return value?.let { Date.from(it.atZone(ZoneId.systemDefault()).toInstant()) }
    }

    @TypeConverter
    fun toLocalDateTime(date: Date?): LocalDateTime? {
        return date?.let { LocalDateTime.ofInstant(it.toInstant(), ZoneId.systemDefault()) }
    }
}