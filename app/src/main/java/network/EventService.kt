package fr.isen.RAVAN.isensmartcompanion.network

import fr.isen.RAVAN.isensmartcompanion.dataBase.NetworkEvent
import retrofit2.Call
import retrofit2.http.GET

interface EventService {
    @GET("events.json")
    fun getEvents(): Call<List<NetworkEvent>>
}