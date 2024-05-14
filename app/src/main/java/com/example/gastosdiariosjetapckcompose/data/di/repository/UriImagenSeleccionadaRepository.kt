package com.example.gastosdiariosjetapckcompose.data.di.repository


import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.data.di.dao.ImagenSeleccionadaDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.ImagenSeleccionadaEntity
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import com.example.gastosdiariosjetapckcompose.domain.model.ImagenSeleccionadaModel
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UriImagenSeleccionadaRepository @Inject constructor(private val imagenSeleccionadaDao: ImagenSeleccionadaDao) {
    suspend fun getImagenGuardada(): ImagenSeleccionadaModel {
        val it = imagenSeleccionadaDao.getImagenSeleccionada()
        return if(it != null){
            ImagenSeleccionadaModel(it.id, it.uri, it.isChecked)
        }else{
            ImagenSeleccionadaModel(id = 0, uri = "",isChecked = true)
        }
    }

    suspend fun insertUriImagenSeleccionada(item: ImagenSeleccionadaModel) {
        imagenSeleccionadaDao.insertImagenSeleccionada(item.toData())
    }

    suspend fun updateImagenSeleccionada(item: ImagenSeleccionadaModel) {
        imagenSeleccionadaDao.updateImagenSeleccionada(item.toData())
    }

    suspend fun deleteUriImagenSeleccionada(item: ImagenSeleccionadaModel) {
        imagenSeleccionadaDao.deleteImagenSeleccionada(item.toData())
    }
}

fun ImagenSeleccionadaModel.toData(): ImagenSeleccionadaEntity {
    return ImagenSeleccionadaEntity(this.id, this.uri)
}