package fr.isen.RAVAN.isensmartcompanion.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_3 = object : Migration(1, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // On crée la table agenda avec les bonnes colonnes.
        database.execSQL("CREATE TABLE IF NOT EXISTS `Agenda` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
    }
}