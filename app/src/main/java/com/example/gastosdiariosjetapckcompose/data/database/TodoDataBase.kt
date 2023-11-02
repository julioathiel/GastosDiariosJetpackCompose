package com.example.gastosdiariosjetapckcompose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gastosdiariosjetapckcompose.data.di.dao.CurrentMoneyDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.FechaSaveDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.MovimientosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.TaskDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.CurrentMoneyEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.FechaSaveEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.MovimientosEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        CurrentMoneyEntity::class,
        FechaSaveEntity::class,
        MovimientosEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TodoDataBase : RoomDatabase() {
    //nos dara el Dao que se creo antes, que es para la base de datos de room
    abstract fun taskDao(): TaskDao
    abstract fun currentMoneyDao(): CurrentMoneyDao
    abstract fun fechaElegidaDao(): FechaSaveDao
    abstract fun movimientosDao(): MovimientosDao
}
