package fr.isen.RAVAN.isensmartcompanion.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "event") // Nom de la table dans la base de données
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Clé primaire, auto-générée
    @ColumnInfo(name = "event_id") val eventId: String = "",
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "date") val date: Date, // Date de l'événement
    @ColumnInfo(name = "end_date") val endDate: Date, // Date de fin de l'événement
    @ColumnInfo(name = "category") val category: String = "",
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "registered") val registered: Boolean,
    @ColumnInfo(name = "is_meeting") val isMeeting: Boolean
) : Parcelable
