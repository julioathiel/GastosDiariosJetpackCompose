package com.example.gastosdiariosjetapckcompose.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gastosdiariosjetapckcompose.data.di.dao.CurrentMoneyDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.FechaSaveDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.GastosPorCategoriaDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.ImagenSeleccionadaDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.MovimientosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.TotalGastosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.TotalIngresosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatGastoDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatIngresoDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.CurrentMoneyEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.FechaSaveEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.GastosPorCategoriaEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.ImagenSeleccionadaEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.MovimientosEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalGastosEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.TotalIngresosEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.UsuarioCreaCatGastoEntity
import com.example.gastosdiariosjetapckcompose.data.di.entity.UsuarioCreaCatIngresoEntity

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
        UsuarioCreaCatIngresoEntity::class
    ],
    version = 1,
    exportSchema = true,
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

}
