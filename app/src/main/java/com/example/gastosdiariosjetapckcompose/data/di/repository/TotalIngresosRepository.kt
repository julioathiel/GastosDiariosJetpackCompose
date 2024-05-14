package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.TotalIngresosDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalIngresosEntity
import com.example.gastosdiariosjetapckcompose.domain.model.TotalIngresosModel
import javax.inject.Inject

class TotalIngresosRepository @Inject constructor(private val totalIngresosDao: TotalIngresosDao) {

    suspend fun getTotalIngresos(): TotalIngresosModel? {
        val get = totalIngresosDao.getTotalIngresos()
        // Devolver un objeto TotalIngresosModel con los datos obtenidos, o null si no se encontraron datos
        return get?.let { TotalIngresosModel(id = it.id, totalIngresos = it.totalIngresos) }
    }

    suspend fun insertTotalIngresos(item: TotalIngresosModel) {
        totalIngresosDao.insertTotalIngresos(item.toData())
    }

    suspend fun updateTotalIngresos(item: TotalIngresosModel) {
        totalIngresosDao.updateTotalIngresos(item.toData())
    }


    suspend fun deleteTotalIngresosRepository(item: TotalIngresosModel) {
        totalIngresosDao.deleteTotalIngresos(item.toData())
    }

    suspend fun isDatabaseTotalIngresosEmpty(): Boolean {
        return totalIngresosDao.getTotalIngresos() == null

        //por ende devolvera un true ya que no hay nada
    }

}

fun TotalIngresosModel.toData(): TotalIngresosEntity {
    return TotalIngresosEntity(this.id, this.totalIngresos)
}
