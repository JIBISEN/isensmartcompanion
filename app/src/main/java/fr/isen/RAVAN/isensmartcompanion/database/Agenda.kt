package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity
@TypeConverters(Converters::class)
data class Agenda(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val location: String? = null,
    @ColumnInfo(name = "date")
    val date: Date = Date()
)
