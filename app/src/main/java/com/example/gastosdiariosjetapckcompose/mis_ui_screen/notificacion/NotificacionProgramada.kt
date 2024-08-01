package com.example.gastosdiariosjetapckcompose.mis_ui_screen.notificacion

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.gastosdiariosjetapckcompose.data.core.Constants.MY_CHANNEL_ID
import com.example.gastosdiariosjetapckcompose.data.core.Constants.NOTIFICATION_ID
import com.example.gastosdiariosjetapckcompose.MainActivity
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.domain.model.NotificationProgramadaModel

class NotificacionProgramada : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationProgramadaModel(
            idNotification = NOTIFICATION_ID,
            smallIcon = R.drawable.ic_ahorro,
            title = context.getString(R.string.title_notification_programada),
            text = context.getString(R.string.textLarge_notification_programada),
            largeIconResId = R.drawable.ic_ahorro,
            priority = NotificationManager.IMPORTANCE_DEFAULT
        )

        createNotification(context, notification)
    }

    private fun createNotification(context: Context, notification: NotificationProgramadaModel) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        //esta parte sirve para abrir la app al presionar la notificacion
        val intent = Intent(context, MainActivity::class.java).apply {
            //crea una nueva bandera y elimina otra existente antes de iniicar la nueva
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.FLAG_IMMUTABLE else 0

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, flag)

        val notifications = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(notification.smallIcon)
            .setContentTitle(notification.title)
            .setContentText(notification.text)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    notification.largeIconResId
                )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(notification.priority)
            .build()

//        notificationManager.notify(idNotification,notification)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notification.idNotification, notifications)


    }
}