package com.example.gastosdiariosjetapckcompose

import android.app.Application
import com.example.gastosdiariosjetapckcompose.data.di.module.DataBaseCleaner
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp:Application(){
    @Inject
    lateinit var databaseCleaner: DataBaseCleaner
    val sharedLogic = GlobalVariables.sharedLogic

//    override fun onCreate() {
//        super.onCreate()
//        // Inyectar y utilizar la instancia de DatabaseCleaner para limpiar la base de datos
//        databaseCleaner.clearDataBase()
//    }
}