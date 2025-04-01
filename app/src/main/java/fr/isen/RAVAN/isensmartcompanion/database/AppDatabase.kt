package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import android.content.Context
import androidx.room.TypeConverters


@Database(entities = [Interaction::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun interactionDao(): InteractionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}