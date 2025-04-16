package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity
@TypeConverters(Converters::class)
data class RendezVous(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    @ColumnInfo(name = "dateDebut")
    val dateDebut: Date,
    @ColumnInfo(name = "dateFin")
    val dateFin: Date,
    val description : String? = null,
    val lieu: String? = null
)