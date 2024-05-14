package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalIngresosEntity

@Dao
interface TotalIngresosDao {
    @Query("SELECT * FROM TotalIngresosEntity LIMIT 1")
    suspend fun getTotalIngresos(): TotalIngresosEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTotalIngresos(totales: TotalIngresosEntity)

    @Update
    suspend fun updateTotalIngresos(totales: TotalIngresosEntity)

    @Delete
    suspend fun deleteTotalIngresos(totales: TotalIngresosEntity)
}