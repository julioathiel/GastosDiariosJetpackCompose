package com.example.gastosdiariosjetapckcompose.features.recordatorio

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.gastosdiariosjetapckcompose.Constants.MY_CHANNEL_ID
import com.example.gastosdiariosjetapckcompose.MainActivity
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.navigation.Routes

class NotificacionProgramada : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 5
    }

    override fun onReceive(context: Context, intent: Intent) {
        crearNotificacion(context)
    }

    private fun crearNotificacion(context: Context) {
        // Crear el canal de notificación si aún no está creado
        crearCanalNotificacion(MY_CHANNEL_ID, context)


        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE else 0

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, flag)

        val notificacion = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ahorro)
            .setContentTitle("Gastos Diarios")
            .setContentText("No olvides agregar las transacciones para llevar un buen control")
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()


        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notificacion)
    }
}