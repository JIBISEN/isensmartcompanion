package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "interactions")
@TypeConverters(Converters::class)
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "timestamp") val timestamp: Date = Date(),
)