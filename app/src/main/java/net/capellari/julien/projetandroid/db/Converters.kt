package net.capellari.julien.projetandroid.db

import androidx.room.TypeConverter
import java.util.*

object Converters {
    // Date
    @TypeConverter
    fun fromTimestamp(timestamp: Long?) = timestamp?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?) = date?.time
}