package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.GastosPorCategoriaDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.GastosPorCategoriaEntity
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GastosPorCategoriaRepository @Inject constructor(private val gastosPorCategoriaDao: GastosPorCategoriaDao) {

    suspend fun insertGastosPorCategoria(item: GastosPorCategoriaModel) {
        gastosPorCategoriaDao.insertGastosPorCategoria(item.toData())
    }

    val categoriaUnica: Flow<List<GastosPorCategoriaModel>> =
        gastosPorCategoriaDao.getGastosPorCategoria().map { items ->
            //por cada uno de esos items, le hacemos otro map
            items.map {
                //ahora por cada una de las iteraciones, creamos un model de datos
                GastosPorCategoriaModel(
                    it.id, it.title, it.categoriaIcon, it.totalGastado
                )
            }
        }

    suspend fun actualizarCategoriaIndividualRepository(
        nombreCategoria: String,
        cantidadIngresada: Double
    ) {
        gastosPorCategoriaDao.actualizarTotalGastadoPorCategoria(nombreCategoria, cantidadIngresada)
    }

    suspend fun updateGastosPorCategoriaRepository(item:GastosPorCategoriaModel){
        gastosPorCategoriaDao.updateGastosPorCategoria(item.toData())
    }


    suspend fun deleteGastosPorCategoriaRepository(item: GastosPorCategoriaModel) {
        gastosPorCategoriaDao.deleteGastosPorCategoria(item.toData())
    }

    suspend fun isDatabaseGastosPorCategoriaEmpty(): Boolean {
        return gastosPorCategoriaDao.getGastosPorCategoria().first().isEmpty()
    }
}

fun GastosPorCategoriaModel.toData(): GastosPorCategoriaEntity {
    return GastosPorCategoriaEntity(
        this.id, this.title, this.categoriaIcon, this.totalGastado
    )
}