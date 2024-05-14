package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.ImagenSeleccionadaEntity

@Dao
interface ImagenSeleccionadaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImagenSeleccionada(uri: ImagenSeleccionadaEntity)

    @Query("SELECT * FROM ImagenSeleccionadaEntity")
    suspend fun getImagenSeleccionada(): ImagenSeleccionadaEntity

    @Update
    suspend fun updateImagenSeleccionada(uri:ImagenSeleccionadaEntity)

    @Delete
    suspend fun deleteImagenSeleccionada(uri:ImagenSeleccionadaEntity)
}