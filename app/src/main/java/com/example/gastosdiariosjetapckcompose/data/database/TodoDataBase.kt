package com.example.gastosdiariosjetapckcompose.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gastosdiariosjetapckcompose.data.di.dao.*
import com.example.gastosdiariosjetapckcompose.data.di.entity.*


@Database(
    entities = [
        CurrentMoneyEntity::class,
        FechaSaveEntity::class,
        MovimientosEntity::class,
        ImagenSeleccionadaEntity::class,
        TotalIngresosEntity::class,
        TotalGastosEntity::class,
        GastosPorCategoriaEntity::class,
        UsuarioCreaCatGastoEntity::class,
        UsuarioCreaCatIngresoEntity::class,
        BarDataEntity::class //nueva tabla
    ],
    version = 2, //cambiando de version 1 a 2
    exportSchema = true,
    autoMigrations = [AutoMigration(from = 1, 2)]
)
abstract class TodoDataBase : RoomDatabase() {
    //nos dara el Dao que se creo antes, que es para la base de datos de room
    abstract fun currentMoneyDao(): CurrentMoneyDao
    abstract fun totalIngresosDao(): TotalIngresosDao
    abstract fun totalGastosDao(): TotalGastosDao
    abstract fun fechaElegidaDao(): FechaSaveDao
    abstract fun movimientosDao(): MovimientosDao
    abstract fun imagenSeleccionadaDao(): ImagenSeleccionadaDao
    abstract fun gastosPorCategoriaDao(): GastosPorCategoriaDao
    abstract fun usuarioCreaCatGastoDao(): UsuarioCreaCatGastoDao
    abstract fun usuarioCreaCatIngresoDao(): UsuarioCreaCatIngresoDao
    abstract fun barDataDao(): BarDataDao //nueva tabla
}

