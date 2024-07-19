package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.MovimientosDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.MovimientosEntity
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovimientosRepository @Inject constructor(private val movimientosDao: MovimientosDao) {

    suspend fun insertMovimientosRepository(item: MovimientosModel) {
        movimientosDao.insertMovimientos(item.toData())
    }

    val movimientos: Flow<List<MovimientosModel>> = movimientosDao.getMovimientos().map { items ->
        //por cada uno de esos items, le hacemos otro map
        items.map {
            //ahora por cada una de las iteraciones, creamos un model de datos
            MovimientosModel(
                it.id, it.iconResourceName,
                it.title, it.subTitle,
                it.cash, it.select,
                it.date
            )
        }
    }

    suspend fun updateMovimientosRepository(movimientosModel: MovimientosModel) {
        movimientosDao.updateMovimientos(movimientosModel.toData())
    }

    suspend fun deleteMovimientosRepository(movimientosModel: MovimientosModel) {
        movimientosDao.deleteMovimientos(movimientosModel.toData())
    }

    suspend fun isDatabaseAllExpensesEmpty(): Boolean {
        return movimientosDao.getMovimientos().first().isEmpty()
    }
}

fun MovimientosModel.toData(): MovimientosEntity {
    return MovimientosEntity(
        this.id, this.iconResourceName,
        this.title, this.subTitle, this.cash,
        this.select, this.date
    )
}