package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.FechaSaveDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.FechaSaveEntity
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import java.time.LocalDate
import javax.inject.Inject

class FechaSaveRepository @Inject constructor(private val fechaSaveDao: FechaSaveDao) {



    suspend fun getFechaElegida(): FechaSaveModel? {
        val it = fechaSaveDao.getFecha()
        return it?.let {
            FechaSaveModel(it.id, it.fechaSave, it.isSelected)
        }
    }

    suspend fun insertFechaElegida(item:FechaSaveModel){
       fechaSaveDao.insertFecha(item.toData())
    }
    suspend fun updateFechaElegida(item:FechaSaveModel){
        fechaSaveDao.updateFecha(item.toData())
    }

    suspend fun deleteFechaElegida(item: FechaSaveModel) {
        fechaSaveDao.deleteFecha(item.toData())
    }

    suspend fun isDatabaseFechaGuardadaEmpty(): Boolean {
        return fechaSaveDao.getFecha() == null
        //si es null devolvera true ya que no hay nada
    }

    suspend fun clearAllFecha(){
        fechaSaveDao.deleteAllDate()
    }

}
fun FechaSaveModel.toData():FechaSaveEntity{
    return FechaSaveEntity(this.id,this.fechaSave,this.isSelected)
}