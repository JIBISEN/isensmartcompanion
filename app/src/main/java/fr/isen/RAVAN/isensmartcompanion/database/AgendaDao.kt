package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendaDao {
    @Query("SELECT * FROM agenda")
    fun getAll(): Flow<List<Agenda>>

    @Insert
    fun insert(agenda: Agenda)

    @Delete
    fun delete(agenda: Agenda)
}