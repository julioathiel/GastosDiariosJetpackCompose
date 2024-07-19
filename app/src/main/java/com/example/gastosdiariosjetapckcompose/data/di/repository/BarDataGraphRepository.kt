package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.BarDataDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.BarDataEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.MovimientosEntity
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BarDataGraphRepository @Inject constructor(private val dao: BarDataDao) {
    val getAllBarDataGraphRepo: Flow<List<BarDataModel>> = dao.getAllBarDataGraph().map { items ->
        //por cada uno de esos items, le hacemos otro map
        items.map {
            //ahora por cada una de las iteraciones, creamos un model de datos
            BarDataModel(it.id, it.value,it.month,it.money)
        }
    }

    suspend fun insertBarDataGraphRepo(item: BarDataModel){
        return dao.insertBarDataGraph(item.toEntity())
    }

    suspend fun updateBarDataGraphRepo(item: BarDataModel) {
        dao.updateBarDataGraph(item.toEntity())
    }

    suspend fun deleteBarDataGraphRepo(item: BarDataModel) {
        dao.deleteBarDataGraph(item.toEntity())
    }

    suspend fun getByIdBarDataGraphRepo(id: Int): BarDataModel {
        val barDataEntity = dao.getByIdBarDataGraph(id)
        return barDataEntity.toModel()
    }

    suspend fun clearBarData() {
        dao.deleteAllBarData()
    }

}
//conviertiendo un objeto Model a Entity
fun BarDataModel.toEntity(): BarDataEntity{
    return BarDataEntity(this.id, this.value,this.month,this.money)
}
//conviertiendo un objeto Entity a Model
fun BarDataEntity.toModel(): BarDataModel {
    return BarDataModel(
        id = this.id,
        value = this.value,
        month = this.month,
        money = this.money
    )
}