package com.example.gastosdiariosjetapckcompose.data.di.module

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DataBaseMigrations {
    //    val MIGRATION_1_TO_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            //se añadio el isCheked despues, por eso es la migration
//           database.execSQL("ALTER TABLE table_moneyEntity ADD COLUMN isCheked INTEGER NOT NULL DEFAULT 0")
//        }
//    }
    //agregando una nueva tabla completa
//    val MIGRATION_2_TO_3 = object : Migration(2, 3) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL(
//                "CREATE TABLE IF NOT EXISTS 'FechaSaveEntity'" +
//                        " (id INTEGER PRIMARY KEY, fechaSave TEXT, isSelected INTEGER NOT NULL DEFAULT 0)"
//            )
//        }
//    }
//    val MIGRATION_3_TO_4 = object : Migration(3, 4) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL(
//                "CREATE TABLE IF NO EXISTS 'MovimientosEntity' " +
//                        "(id INTEGER PRIMARY KEY, imageResource: Int," +
//                        " title: TEXT," +
//                        " subTitle: TEXT," +
//                        " cash: TEXT," +
//                        " select: INTEGER NOT NULL DEFAULT 0)"
//            )
//        }
//    }
}