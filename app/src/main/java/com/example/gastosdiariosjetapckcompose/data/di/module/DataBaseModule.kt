package com.example.gastosdiariosjetapckcompose.data.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.gastosdiariosjetapckcompose.data.database.TodoDataBase
import com.example.gastosdiariosjetapckcompose.data.di.dao.BarDataDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.CurrentMoneyDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.FechaSaveDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.GastosPorCategoriaDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.ImagenSeleccionadaDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.MovimientosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.TotalGastosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.TotalIngresosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatGastoDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatIngresoDao
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideTaskDataBase(@ApplicationContext appContext: Context): TodoDataBase {
        //necesita el contexto y donde esta la clase y el nombre de la clase
        return Room.databaseBuilder(appContext, TodoDataBase::class.java, "GastosdiariosDatabase")
            .build()
    }

    @Provides
    @Singleton
    // se necesita para eliminar la base de datros desde MyApp
    fun provideDatabaseCleaner(@ApplicationContext appContext: Context): DataBaseCleaner {
        return DataBaseCleaner(appContext)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideCurrentMoneyDao(database: TodoDataBase): CurrentMoneyDao {
        return database.currentMoneyDao()
    }

    @Provides
    @Singleton
    fun provideFechaElegidaDao(database: TodoDataBase): FechaSaveDao {
        return database.fechaElegidaDao()
    }

    @Provides
    @Singleton
    fun provideMovimientosDao(database: TodoDataBase): MovimientosDao {
        return database.movimientosDao()
    }

    @Provides
    @Singleton
    fun provideImagenSeleccionadaDao(database: TodoDataBase): ImagenSeleccionadaDao {
        return database.imagenSeleccionadaDao()
    }

    @Provides
    @Singleton
    fun provideTotalIngresosDao(database: TodoDataBase): TotalIngresosDao {
        return database.totalIngresosDao()
    }

    @Provides
    @Singleton
    fun provideTotalGastosDao(database: TodoDataBase): TotalGastosDao {
        return database.totalGastosDao()
    }

    @Provides
    @Singleton
    fun provideGastosPorCategoriaDao(database: TodoDataBase): GastosPorCategoriaDao {
        return database.gastosPorCategoriaDao()
    }

    @Provides
    @Singleton
    fun provideUsuarioCreaCatGastoDao(database: TodoDataBase): UsuarioCreaCatGastoDao {
        return database.usuarioCreaCatGastoDao()
    }

    @Provides
    @Singleton
    fun provideUsuarioCreaCatIngresoDao(database: TodoDataBase): UsuarioCreaCatIngresoDao {
        return database.usuarioCreaCatIngresoDao()
    }

    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext context: Context): DataStorePreferences {
        return DataStorePreferences(context)
    }


    @Provides
    @Singleton
    fun provideBarDataGraph(database: TodoDataBase): BarDataDao {
        return database.barDataDao() //nueva tabla
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
