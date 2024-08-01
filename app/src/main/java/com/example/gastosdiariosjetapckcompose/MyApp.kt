package com.example.gastosdiariosjetapckcompose

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.gastosdiariosjetapckcompose.data.core.Constants.DESCRIPTION_TEXT_NOTIFICATION_CANAL
import com.example.gastosdiariosjetapckcompose.data.core.Constants.MY_CHANNEL_ID
import com.example.gastosdiariosjetapckcompose.data.core.Constants.NAME_NOTIFICATION_CANAL
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables
import com.example.gastosdiariosjetapckcompose.data.di.module.DataBaseCleaner
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var databaseCleaner: DataBaseCleaner
    val sharedLogic = GlobalVariables.sharedLogic

    override fun onCreate() {
        super.onCreate()
        // Inyectar y utilizar la instancia de DatabaseCleaner para limpiar la base de datos
     //   databaseCleaner.clearDataBase()
       createCanalNotification()
    }

    private fun createCanalNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val channel = NotificationChannel(
                MY_CHANNEL_ID,
                NAME_NOTIFICATION_CANAL,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = DESCRIPTION_TEXT_NOTIFICATION_CANAL
            }
            
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}