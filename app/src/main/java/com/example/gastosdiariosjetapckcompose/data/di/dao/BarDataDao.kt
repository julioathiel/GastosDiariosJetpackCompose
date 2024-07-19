package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.BarDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BarDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarDataGraph(item: BarDataEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBarDataGraph(item: BarDataEntity)

    @Query("select * from BarDataEntity where id = :id")
    suspend fun getByIdBarDataGraph(id: Int): BarDataEntity

    @Query("select * from BarDataEntity")
    fun getAllBarDataGraph(): Flow<List<BarDataEntity>>

    @Delete
    suspend fun deleteBarDataGraph(item: BarDataEntity)

    @Query("DELETE FROM BarDataEntity")
    suspend fun deleteAllBarData()
}