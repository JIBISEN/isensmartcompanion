package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<Event>)

    @Query("SELECT * FROM event")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getEventById(eventId: Int): Flow<Event?>

    @Query("DELETE FROM event WHERE id = :eventId")
    suspend fun deleteEventById(eventId: Int)
}