package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "inscription",
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Inscription(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "eventId") val eventId: Int,
    @ColumnInfo(name = "userId") val userId: Int
)