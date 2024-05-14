package com.example.gastosdiariosjetapckcompose.domain.usecase

import android.util.Log
import com.example.gastosdiariosjetapckcompose.data.di.repository.GastosPorCategoriaRepository
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GastosPorNameCatCaseUse @Inject constructor(
    private val gastosPorCategoriaRepository: GastosPorCategoriaRepository
) {
    suspend operator fun invoke(nameCategory: String, icon: String, cantidadIngresada: String) {
        // Convertir la cantidad ingresada a Double
        val cantidadDouble = cantidadIngresada.toDoubleOrNull() ?: return // Retorna si no se puede convertir

        // Obtener la lista de categorías del flujo
        val categoriaUnica = gastosPorCategoriaRepository.categoriaUnica.firstOrNull()

        // Verificar si la lista de categorías unicas está vacía
        if (categoriaUnica.isNullOrEmpty()) {
            // Si está vacía, insertamos una nueva categoría y el gasto
            gastosPorCategoriaRepository.insertGastosPorCategoria(
                GastosPorCategoriaModel(
                    title = nameCategory,
                    categoriaIcon = icon,
                    totalGastado = cantidadIngresada
                )
            )
        } else {
            // Buscar si la categoría ya existe en la lista de categorías
            val categoriaExistente = categoriaUnica.find { it.title == nameCategory }

            if (categoriaExistente == null) {
                // Si no existe, insertamos una nueva categoría y el gasto
                gastosPorCategoriaRepository.insertGastosPorCategoria(
                    GastosPorCategoriaModel(
                        title = nameCategory,
                        categoriaIcon = icon,
                        totalGastado = cantidadIngresada
                    )
                )
            } else {
                // Si la categoría existe, actualizamos el total gastado de la categoría
                gastosPorCategoriaRepository.actualizarCategoriaIndividualRepository(
                    nombreCategoria = nameCategory, cantidadIngresada = cantidadDouble)

                Log.d("baseD", "actualizando: $cantidadDouble")
            }
        }
    }
}
