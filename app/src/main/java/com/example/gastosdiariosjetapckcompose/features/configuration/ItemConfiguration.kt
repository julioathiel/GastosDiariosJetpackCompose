package com.example.gastosdiariosjetapckcompose.features.configuration

import androidx.annotation.DrawableRes
import com.example.gastosdiariosjetapckcompose.R

enum class ItemConfiguration(
    @get:DrawableRes
    var icon: Int,
    val title: Int,
    val description: Int,
    val category:String
) {
    CATEGORIASNUEVAS(R.drawable.ic_folder, R.string.create_category_title, R.string.create_category_description, "Principal"),
    UPDATEDATE(R.drawable.ic_date_range, R.string.update_date_title, R.string.update_date_description, "Principal"),
    RECORDATORIOS(R.drawable.ic_notifications, R.string.notifications_title, R.string.notifications_description, "Categoría 2"),
    RESET(R.drawable.ic_refresh, R.string.reset_title, R.string.reset_description, "Categoría 2"),
    TUTORIAL(R.drawable.ic_play, R.string.tutorial_title, R.string.tutorial_description, "Categoría 2"),
    COMPARTIR(R.drawable.ic_share, R.string.share_app_title, R.string.share_app_description, "Información y ayuda"),
    ACERCADE(R.drawable.ic_info, R.string.about_title, R.string.about_description, "Información y ayuda"),
    AJUSTES_AVANZADOS(R.drawable.ic_settings, R.string.advanced_settings_title, R.string.advanced_settings_description, "Configuración avanzada"),
    EXPORTAR_DATOS(R.drawable.ic_export, R.string.export_data_title, R.string.export_data_description, "Configuración avanzada"),
//    IMPORTAR_DATOS(R.drawable.ic_import, R.string.import_data_title, R.string.import_data_description, "Configuración avanzada"),
//    CENTRO_AYUDA(R.drawable.ic_otros, R.string.help_center_title, R.string.help_center_description, "Ayuda y soporte"),
//    ENVIAR_COMENTARIO(R.drawable.ic_feedback, R.string.send_feedback_title, R.string.send_feedback_description, "Ayuda y soporte"),
//    REPORTAR_PROBLEMA(R.drawable.ic_report, R.string.report_issue_title, R.string.report_issue_description, "Ayuda y soporte"),
//    IDIOMA(R.drawable.ic_language, R.string.language_title, R.string.language_description, "Otros ajustes"),
//    NOTIFICACIONES(R.drawable.ic_notifications, R.string.notifications_title, R.string.notifications_description, "Otros ajustes"),
//    CONTRASEÑA(R.drawable.ic_lock, R.string.password_title, R.string.password_description, "Otros ajustes")
}