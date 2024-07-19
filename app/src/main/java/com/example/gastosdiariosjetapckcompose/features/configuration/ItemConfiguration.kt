package com.example.gastosdiariosjetapckcompose.features.configuration

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.example.gastosdiariosjetapckcompose.R

enum class ItemConfiguration(
    @get:DrawableRes
    var icon: Int,
    val title: String,
    val descripcion:String
) {
    CATEGORIASNUEVAS( R.drawable.ic_folder,"Categorias nuevas","Aqui encontras las categorias que tu crees"),
    UPDATEDATE(R.drawable.ic_date_range,"Actualizar maximo fecha", "Para alcanzar tus objetivos"),
    RECORDATORIOS(R.drawable.ic_notifications,"Recordatorios","Modificar la alarma"),
    RESET(R.drawable.ic_refresh,"Resetear aplicacion","Elimina tood de la app"),
    TUTORIAL(R.drawable.ic_play,"Tutorial","Ver tutorial de como usar la app."),
    COMPARTIR(R.drawable.ic_share,"Compartir app","comparte la app"),
    ACERCADE(R.drawable.ic_info,"Acerca de...","Un breve repaso"),
}