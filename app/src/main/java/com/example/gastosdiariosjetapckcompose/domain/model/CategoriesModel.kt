package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.annotation.DrawableRes
import com.example.gastosdiariosjetapckcompose.R

interface CategoriesModel {
    val name: String
    @get:DrawableRes
    var icon: Int
}

data class CategoryIngreso(override val name: String, override var icon: Int) : CategoriesModel
data class CategoryGasto(override val name: String, override var icon: Int) : CategoriesModel
data class CategoryDefault(override val name: String, override var icon: Int) : CategoriesModel
data class CategoryCrear(override val name: String, override var icon: Int) : CategoriesModel

val categoriaDefault = listOf(
    CategoryDefault("Elige una categoria", R.drawable.ic_empty)
)

val categoriesIngresos = mutableListOf(
    CategoryIngreso("Sueldo", R.drawable.ic_sueldo),
    CategoryIngreso("Deposito", R.drawable.ic_banco),
    CategoryIngreso("Ahorros", R.drawable.ic_ahorro)
)

val categoriesGastos = mutableListOf(
    CategoryGasto("Alquiler/Hipoteca", R.drawable.ic_alquiler_hipoteca),
    CategoryGasto("Transporte", R.drawable.ic_transporte),
    CategoryGasto("Hogar", R.drawable.ic_home),
    CategoryGasto("Trabajo", R.drawable.ic_trabajo),
    CategoryGasto("Combustible", R.drawable.ic_combustible),
    CategoryGasto("Supermercado", R.drawable.ic_supermercado),
    CategoryGasto("Alimentos", R.drawable.ic_tienda_comestibles),
    CategoryGasto("Tarjeta", R.drawable.ic_tarjeta_credito),
    CategoryGasto("Shopping", R.drawable.ic_shopping),
    CategoryGasto("Farmacia", R.drawable.ic_local_pharmacy),
    CategoryGasto("Automovil", R.drawable.ic_automovil),
    CategoryGasto("Restaurante", R.drawable.ic_restaurante),
    CategoryGasto("Doctor/a", R.drawable.ic_doctor),
    CategoryGasto("Entretenimiento", R.drawable.ic_entretenimiento),
    CategoryGasto("Mascota", R.drawable.ic_mascota),
    CategoryGasto("Vacaciones", R.drawable.ic_vacaciones),
    CategoryGasto("Vestimenta", R.drawable.ic_vestimenta),
    CategoryGasto("Servicios", R.drawable.ic_recibo),
    CategoryGasto("Educacion", R.drawable.ic_school),
    CategoryGasto("Telefonia", R.drawable.ic_call),
)

//no poner nombres sino no poder encontrar icono seleccionado al editar
val categorieGastosNuevos = listOf(
    CategoryCrear("", R.drawable.ic_pescar),
    CategoryCrear("", R.drawable.ic_natacion),
    CategoryCrear("", R.drawable.ic_futbol),
    CategoryCrear("", R.drawable.ic_local_cafe),
    CategoryCrear("", R.drawable.ic_local_pizza),
    CategoryCrear("", R.drawable.ic_helado),
    CategoryCrear("", R.drawable.ic_pastel),
    CategoryCrear("", R.drawable.ic_tarjeta_credito),
    CategoryCrear("", R.drawable.ic_regalos),
    CategoryCrear("", R.drawable.ic_avion),
    CategoryCrear("", R.drawable.ic_bicicleta),
    CategoryCrear("", R.drawable.ic_bar_vino),
    CategoryCrear("", R.drawable.ic_ahorro),
    CategoryCrear("", R.drawable.ic_cigarrillos),
    CategoryCrear("", R.drawable.ic_comida_rapida),
    CategoryCrear("", R.drawable.ic_hotel),
    CategoryCrear("", R.drawable.ic_restaurante),
    CategoryCrear("", R.drawable.ic_alquiler_hipoteca),
    CategoryCrear("", R.drawable.ic_local_car_wash),
    CategoryCrear("", R.drawable.ic_transporte),
    CategoryCrear("", R.drawable.ic_auto_electrico),
    CategoryCrear("", R.drawable.ic_home),
    CategoryCrear("", R.drawable.ic_trabajo),
    CategoryCrear("", R.drawable.ic_ev_station_electrica),
    CategoryCrear("", R.drawable.ic_combustible_gas),
    CategoryCrear("", R.drawable.ic_combustible),
    CategoryCrear("", R.drawable.ic_supermercado),
    CategoryCrear("", R.drawable.ic_tienda_comestibles),
    CategoryCrear("", R.drawable.ic_shopping),
    CategoryCrear("", R.drawable.ic_hospital),
    CategoryCrear("", R.drawable.ic_local_pharmacy),
    CategoryCrear("", R.drawable.ic_automovil),
    CategoryCrear("", R.drawable.ic_doctor),
    CategoryCrear("", R.drawable.ic_entretenimiento),
    CategoryCrear("", R.drawable.ic_mascota),
    CategoryCrear("", R.drawable.ic_construccion),
    CategoryCrear("", R.drawable.ic_vacaciones),
    CategoryCrear("", R.drawable.ic_vestimenta),
    CategoryCrear("", R.drawable.ic_recibo),
    CategoryCrear("", R.drawable.ic_school),
    CategoryCrear("", R.drawable.ic_otros),
    CategoryCrear("", R.drawable.ic_electrical_services),
    CategoryCrear("", R.drawable.ic_call),
    CategoryCrear("", R.drawable.ic_estadio),
    CategoryCrear("", R.drawable.ic_apartamento),
    CategoryCrear("", R.drawable.ic_asistencia_anciano),
    CategoryCrear("", R.drawable.ic_cerveza),
    CategoryCrear("", R.drawable.ic_barra_filled),
    CategoryCrear("", R.drawable.ic_lavadero_ropa),
    CategoryCrear("", R.drawable.ic_corazon),
    CategoryCrear("", R.drawable.ic_folder),
)
