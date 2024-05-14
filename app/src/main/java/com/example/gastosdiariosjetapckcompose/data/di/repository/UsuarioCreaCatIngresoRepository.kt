package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatIngresoDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.UsuarioCreaCatIngresoEntity
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatIngresosModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class UsuarioCreaCatIngresoRepository @Inject constructor(private val usuarioCreaCatIngresoDao: UsuarioCreaCatIngresoDao) {
    suspend fun insertUsuarioCreaCatIngresosRepository(item: UsuarioCreaCatIngresosModel) {
        usuarioCreaCatIngresoDao.insertUsuarioCreaCatIngreso(item.toData())
    }

    val listaCategoriaUnicaIngresos: Flow<List<UsuarioCreaCatIngresosModel>> = usuarioCreaCatIngresoDao.getMovimientos().map { items ->
        //por cada uno de esos items, le hacemos otro map
        items.map {
            //ahora por cada una de las iteraciones, creamos un model de datos
            UsuarioCreaCatIngresosModel(
                it.id, it.nombreCategoria,it.categoriaIcon
            )
        }
    }

    suspend fun updateUsuarioCreaCatIngresosRepository(item: UsuarioCreaCatIngresosModel){
        usuarioCreaCatIngresoDao.updateUsuarioCreaCatIngreso(item.toData())
    }

    suspend fun deleteUsuarioCreaCatIngresosRepository(item: UsuarioCreaCatIngresosModel){
        usuarioCreaCatIngresoDao.deleteUsuarioCreaCatIngreso(item.toData())
    }
}

fun UsuarioCreaCatIngresosModel.toData(): UsuarioCreaCatIngresoEntity {
    return UsuarioCreaCatIngresoEntity(
        this.id, this.nombreCategoria,this.categoriaIcon
    )
}