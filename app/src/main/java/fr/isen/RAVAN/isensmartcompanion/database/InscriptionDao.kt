package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InscriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInscription(inscription: Inscription)

    @Query("SELECT * FROM inscription WHERE userId = :userId")
    fun getInscriptionsByUserId(userId: Int): Flow<List<Inscription>>

    @Query("DELETE FROM inscription WHERE eventId = :eventId AND userId = :userId")
    suspend fun deleteInscription(eventId: Int, userId: Int)
}