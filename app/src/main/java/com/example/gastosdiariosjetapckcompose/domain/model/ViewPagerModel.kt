package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.annotation.DrawableRes
import com.example.gastosdiariosjetapckcompose.R

data class ViewPagerModel(
    @get:DrawableRes
    var image: Int,
    val title: String,
    val descripcion: String
)

val view = listOf(
    ViewPagerModel(
        R.raw.pager1,
        "Controla tus gastos diarios facilmente",
        "Gestiona tus finanzas de forma sencilla y eficiente con nuestra aplicación de control de gastos diarios."
    ),
    ViewPagerModel(
        R.raw.congratulation_lottie,
        "Suerte",
        "Ingresa con confianza"
    ),
//    ViewPagerModel(
//        R.drawable.image_viewpager_home,
//        "Beneficios de usar nuestra aplicación",
//        "Estadísticas detalladas"
//    ),
//    ViewPagerModel(
//        R.drawable.image_viewpager_dinerodia_dinero_actual,
//        "Tu dinero Actual y lo que puedes gastar por dia",
//        "Puedes ver tu dinero total y lo que puedes gastor por dia, asi como los dias restantees hacia le fecha que seleccionaste."
//    ),
//    ViewPagerModel(
//        R.drawable.image_viewpager_calendario,
//        "Beneficios de usar nuestra aplicación",
//        "Presionando Editar puedes cambiar el dia para contar hasta ese dia tu dinero."
//    ),
//    ViewPagerModel(
//        R.drawable.image_viewpager_total_ingresos_gastos,
//        "Ver el total de los ingrsos y gastos",
//        "Estadísticas detalladas"
//    ),
//    ViewPagerModel(
//        R.drawable.image_view_pager_gastos_por_categoria,
//        "Ver Gastos por categoria",
//        "Presionando en ver mas,encontraras una lista detallada con los gastos por cada categoria, ademas de mostrar en caul categorias vas gastando mas en el mes."
//    ),
//    ViewPagerModel(
//        R.drawable.image_viewpager_creando_categoria,
//        "Crea nueva categoria tanto de ingresos como gastos",
//        "Puedes crear, editar, eliminar, hasta vaciar toda la lista que hayas creado."
//    ),
//    ViewPagerModel(
//        R.drawable.image_viewpager_cambiar_mes,
//        "Puedes cambiar hasta que mes controlar tu dinero",
//        "Selecciona entre 31, 60 o 90 dias, predeterminada se encuentra por mes."
//    ),
//    ViewPagerModel(
//        R.drawable.image_notification_personalizada,
//        "Notificaciones personalizadas",
//        "Cambia la hora que se enviará la notificacion."
//    ),
)
