package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.UsuarioCreaCatIngresoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioCreaCatIngresoDao {
    @Query("SELECT * from UsuarioCreaCatIngresoEntity")
    fun getMovimientos(): Flow<List<UsuarioCreaCatIngresoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarioCreaCatIngreso(item: UsuarioCreaCatIngresoEntity)

    @Update
    suspend fun updateUsuarioCreaCatIngreso(item: UsuarioCreaCatIngresoEntity)

    @Delete
    suspend fun deleteUsuarioCreaCatIngreso(item: UsuarioCreaCatIngresoEntity)

    @Query("delete from UsuarioCreaCatIngresoEntity")
    suspend fun deleteAllUsuarioCreaCatIngresos()
}