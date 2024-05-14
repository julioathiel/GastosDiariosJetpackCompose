package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.GastosPorCategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastosPorCategoriaDao {

    @Query("SELECT * from GastosPorCategoriaEntity")
    fun getGastosPorCategoria(): Flow<List<GastosPorCategoriaEntity>>

    @Query("Update GastosPorCategoriaEntity SET totalGastado = totalGastado + :cantidad WHERE title = :nombre")
    suspend fun actualizarTotalGastadoPorCategoria(nombre: String, cantidad: Double)

    @Insert
    suspend fun insertGastosPorCategoria(item: GastosPorCategoriaEntity)

    @Update
    suspend fun updateGastosPorCategoria(item: GastosPorCategoriaEntity)

    @Delete
    suspend fun deleteGastosPorCategoria(item: GastosPorCategoriaEntity)
}