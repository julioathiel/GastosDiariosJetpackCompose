package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.FechaSaveEntity

@Dao
interface FechaSaveDao {
    @Query("Select * from FechaSaveEntity")
    suspend fun getFecha(): FechaSaveEntity

    @Insert
    suspend fun insertFecha(item: FechaSaveEntity)

    @Update
    suspend fun updateFecha(item: FechaSaveEntity)

    @Delete
    suspend fun deleteFecha(item: FechaSaveEntity)

    @Query("delete from FechaSaveEntity")
    suspend fun deleteAllDate()
}