package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatGastoDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.UsuarioCreaCatGastoEntity
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioCreaCatGastoRepository @Inject constructor(private val usuarioCreaCatGastoDao: UsuarioCreaCatGastoDao) {
    suspend fun insertUsuarioCreaCatGastoRepository(item: UsuarioCreaCatGastoModel) {
        usuarioCreaCatGastoDao.insertUsuarioCreaCatGasto(item.toData())
    }

    val listaCategoriaUnicaGastos: Flow<List<UsuarioCreaCatGastoModel>> = usuarioCreaCatGastoDao.getMovimientos().map { items ->
        //por cada uno de esos items, le hacemos otro map
        items.map {
            //ahora por cada una de las iteraciones, creamos un model de datos
            UsuarioCreaCatGastoModel(
                it.id, it.nombreCategoria,it.categoriaIcon
            )
        }
    }

    suspend fun updateUsuarioCreaCatGastoRepository(item: UsuarioCreaCatGastoModel){
        usuarioCreaCatGastoDao.updateUsuarioCreaCatGasto(item.toData())
    }

    suspend fun deleteUsuarioCreaCatGastoRepository(item: UsuarioCreaCatGastoModel){
        usuarioCreaCatGastoDao.deleteUsuarioCreaCatGasto(item.toData())
    }
}

fun UsuarioCreaCatGastoModel.toData(): UsuarioCreaCatGastoEntity {
    return UsuarioCreaCatGastoEntity(
        this.id, this.nombreCategoria,this.categoriaIcon
    )
}