package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.MovimientosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientosDao {
    @Query("SELECT * from MovimientosEntity")
    fun getMovimientos(): Flow<List<MovimientosEntity>>

    @Insert
    suspend fun insertMovimientos(item: MovimientosEntity)

    @Update
    suspend fun updateMovimientos(item: MovimientosEntity)

    @Delete
    suspend fun deleteMovimientos(item: MovimientosEntity)

    @Query("delete from MovimientosEntity")
    suspend fun deleteAllMovimientos()
}