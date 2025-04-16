package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RendezVousDao {
    @Query("SELECT * FROM rendezvous WHERE userId = :userId")
    fun getRendezVousByUserId(userId: Int): Flow<List<RendezVous>>

    @Insert
    suspend fun insertRendezVous(rendezVous: RendezVous)

    @Delete
    suspend fun deleteRendezVous(rendezVous: RendezVous)
}