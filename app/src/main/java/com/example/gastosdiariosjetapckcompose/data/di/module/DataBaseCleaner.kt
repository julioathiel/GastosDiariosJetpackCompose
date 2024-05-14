package com.example.gastosdiariosjetapckcompose.data.di.module

import android.content.Context

class DataBaseCleaner(private val context: Context) {
    fun clearDataBase(){
        val databaseNames = listOf("GastosdiariosDatabase")

        databaseNames.forEach { dbName ->
            val databaseFile = context.getDatabasePath(dbName)
            if (databaseFile.exists()) {
                databaseFile.delete()
            }
        }
    }
}