package com.example.gastosdiariosjetapckcompose.data.di.module

import android.content.Context
import androidx.room.Room
import com.example.gastosdiariosjetapckcompose.data.database.TodoDataBase
import com.example.gastosdiariosjetapckcompose.data.di.dao.CurrentMoneyDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.FechaSaveDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.MovimientosDao
import com.example.gastosdiariosjetapckcompose.data.di.dao.TaskDao

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
           // .addMigrations(DataBaseMigrations.MIGRATION_3_TO_4)
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabaseCleaner(@ApplicationContext appContext: Context): DataBaseCleaner {
        return DataBaseCleaner(appContext)
    }

    @Provides
    fun providesTaskDao(database: TodoDataBase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideCurrentMoneyDao(database: TodoDataBase): CurrentMoneyDao {
        return database.currentMoneyDao()
    }

    @Provides
    @Singleton
    fun provideFechaElegidaDao(database: TodoDataBase):FechaSaveDao{
        return database.fechaElegidaDao()
    }

    @Provides
    @Singleton
    fun provideMovimientosDao(database: TodoDataBase):MovimientosDao{
        return  database.movimientosDao()
    }
}
