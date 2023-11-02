package com.example.gastosdiariosjetapckcompose.domain.model

import androidx.annotation.DrawableRes
import com.example.gastosdiariosjetapckcompose.R

interface Category {
    val name: String
    @get:DrawableRes
    var icon:Int
}

data class CategoryIngreso(override val name: String, override var icon: Int) : Category
data class CategoryGasto(override val name: String, override var icon: Int) : Category

val categoriesIngresos = listOf(
    CategoryIngreso("Sueldo", R.drawable.ic_sueldo),
    CategoryIngreso("Deposito", R.drawable.ic_banco),
    CategoryIngreso("Ahorros", R.drawable.ic_ahorro),
    CategoryIngreso("Otros", R.drawable.ic_otros),
)

val categoriesGastos = listOf(
    CategoryGasto("Hogar", R.drawable.ic_home),
    CategoryGasto("Trabajo", R.drawable.ic_trabajo),
    CategoryGasto("Combustible", R.drawable.ic_combustible),
    CategoryGasto("Supermercado", R.drawable.ic_supermercado),
    CategoryGasto("Alimentos", R.drawable.ic_alimentos),
    CategoryGasto("Tarjeta", R.drawable.ic_tarjeta_credito),
    CategoryGasto("Shopping", R.drawable.ic_shopping),
    CategoryGasto("Farmacia", R.drawable.ic_local_pharmacy),
    CategoryGasto("Automovil", R.drawable.ic_automovil),
    CategoryGasto("Restaurante", R.drawable.ic_restaurante),
    CategoryGasto("Doctor/a", R.drawable.ic_doctor),
    CategoryGasto("Entretenimiento", R.drawable.ic_entretenimiento),
    CategoryGasto("Mascota", R.drawable.ic_mascota),
    CategoryGasto("Bicicleta", R.drawable.ic_bicicleta),
    CategoryGasto("Comida rapida", R.drawable.ic_comida_rapida),
    CategoryGasto("Construccion", R.drawable.ic_construccion),
    CategoryGasto("Heladeria", R.drawable.ic_helado),
    CategoryGasto("Hotel", R.drawable.ic_hotel),
    CategoryGasto("Regalos", R.drawable.ic_regalos),
    CategoryGasto("Vacaciones", R.drawable.ic_vacaciones),
    CategoryGasto("Vestimenta", R.drawable.ic_vestimenta),
    CategoryGasto("Viajes", R.drawable.ic_viajes),
    CategoryGasto("Otros", R.drawable.ic_otros),
)
