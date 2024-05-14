package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.UsuarioCreaCatGastoEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface UsuarioCreaCatGastoDao {
    @Query("SELECT * from UsuarioCreaCatGastoEntity")
    fun getMovimientos(): Flow<List<UsuarioCreaCatGastoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuarioCreaCatGasto(item: UsuarioCreaCatGastoEntity)

    @Update
    suspend fun updateUsuarioCreaCatGasto(item: UsuarioCreaCatGastoEntity)

    @Delete
    suspend fun deleteUsuarioCreaCatGasto(item: UsuarioCreaCatGastoEntity)

}