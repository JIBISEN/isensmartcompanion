package fr.isen.RAVAN.isensmartcompanion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.isen.RAVAN.isensmartcompanion.Converters

@Database(entities = [Interaction::class, Agenda::class], version = 3, exportSchema = false) // Increment version to 3
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interactionDao(): InteractionDao
    abstract fun agendaDao(): AgendaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_3)
                    .fallbackToDestructiveMigration() // Ajout de cette ligne
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}