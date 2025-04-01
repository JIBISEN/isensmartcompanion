package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "interactions")
data class Interaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val answer: String,
    val timestamp: Date = Date()
)