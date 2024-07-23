package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalGastosEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalIngresosEntity

@Dao
interface TotalGastosDao {
    @Query("SELECT * FROM TotalGastosEntity LIMIT 1")
    suspend fun getTotalGastos(): TotalGastosEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalGastos(totales: TotalGastosEntity)

    @Update
    suspend fun updateTotalGastos(totales: TotalGastosEntity)

    @Delete
    suspend fun deleteTotalGastos(totales: TotalGastosEntity)

    @Query("delete from TotalGastosEntity")
    suspend fun deleteAllTotalGastos()
}