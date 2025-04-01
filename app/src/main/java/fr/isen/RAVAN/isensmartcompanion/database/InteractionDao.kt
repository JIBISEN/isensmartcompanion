package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionDao {
    @Insert
    suspend fun insert(interaction: Interaction)

    @Delete
    suspend fun delete(interaction: Interaction)

    @Query("DELETE FROM interactions")
    suspend fun deleteAll()

    @Query("SELECT * FROM interactions ORDER BY timestamp DESC")
    fun getAllInteractions(): Flow<List<Interaction>>
}