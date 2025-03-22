package fr.isen.RAVAN.isensmartcompanion.dataBase

import com.google.gson.annotations.SerializedName
import android.util.Log
import fr.isen.RAVAN.isensmartcompanion.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class NetworkEvent(
    @SerializedName("category") val category: String,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("location") val location: String,
    @SerializedName("title") val title: String
)

fun NetworkEvent.toEvent(): Event {
    return Event(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.date,
        location = this.location,
        category = this.category
    )
}