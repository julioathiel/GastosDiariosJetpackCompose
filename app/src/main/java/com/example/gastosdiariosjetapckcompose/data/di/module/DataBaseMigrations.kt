package com.example.gastosdiariosjetapckcompose.data.di.module

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gastosdiariosjetapckcompose.R

object DataBaseMigrations {
val MIGRATION_1_TO_2 = object : Migration(1,2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS 'UsuarioCreaCatIngresoEntity' " +
                        "(id INTEGER PRIMARY KEY," +
                        " nombreCategoria TEXT," +
                        " categoriaIcon TEXT)"
            )
        }
    }
}