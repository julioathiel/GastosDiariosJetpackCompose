package com.example.gastosdiariosjetapckcompose.data.di.module

import android.content.Context

class DataBaseCleaner(private val context: Context) {
    fun clearDataBase(){
        val databaseFile = context.getDatabasePath("GastosdiariosDatabase")
        if (databaseFile.exists()) {
            databaseFile.delete()
        }
    }
}