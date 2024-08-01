package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.TotalGastosDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalGastosEntity
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import javax.inject.Inject

class TotalGastosRepository @Inject constructor(private val totalGastosDao: TotalGastosDao) {

    suspend fun getTotalGastos(): TotalGastosModel? {
        val get = totalGastosDao.getTotalGastos()
        if (get == null) {
            return null
        }
        return TotalGastosModel(id = get.id, totalGastos = get.totalGastos)
    }

    suspend fun insertTotalGastos(item: TotalGastosModel) {
        totalGastosDao.insertTotalGastos(item.toData())
    }

    suspend fun updateTotalGastos(item: TotalGastosModel) {
        totalGastosDao.updateTotalGastos(item.toData())
    }

    suspend fun deleteTotalGastos(item: TotalGastosModel) {
        totalGastosDao.deleteTotalGastos(item.toData())
    }

    suspend fun clearAllTotalGastosRepository(){
        totalGastosDao.deleteAllTotalGastos()
    }

}

fun TotalGastosModel.toData(): TotalGastosEntity {
    return TotalGastosEntity(this.id, this.totalGastos)
}

