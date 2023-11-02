package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.FechaSaveDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.FechaSaveEntity
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import java.time.LocalDate
import javax.inject.Inject

class FechaSaveRepository @Inject constructor(private val fechaSaveDao: FechaSaveDao) {

    suspend fun getFechaElegida(): FechaSaveModel {
        val it = fechaSaveDao.getFecha()
        return if(it != null){
            FechaSaveModel(it.id, it.fechaSave, it.isSelected)
        }else{
            // Manejando el caso en el que it es nulo
            // (primera vez que el usuario agrega algo a la base de datos)
            FechaSaveModel(id = 1, fechaSave = LocalDate.now().toString(), isSelected = true)
        }
    }

    suspend fun insertFechaElegida(item:FechaSaveModel){
       fechaSaveDao.insertFecha(item.toData())
    }
    suspend fun updateFechaElegida(item:FechaSaveModel){
        fechaSaveDao.updateFecha(item.toData())
    }
}
fun FechaSaveModel.toData():FechaSaveEntity{
    return FechaSaveEntity(this.id,this.fechaSave,this.isSelected)
}