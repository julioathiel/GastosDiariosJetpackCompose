package com.example.gastosdiariosjetapckcompose.data.di.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gastosdiariosjetapckcompose.data.di.entity.CurrentMoneyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentMoneyDao {
    @Query("SELECT * from currentMoneyEntity LIMIT 1")
    suspend fun getMoney(): CurrentMoneyEntity
    //se utiliza para recuperar el registro de dinero actual guardado en la base de datos
    //LIMIT 1 significa que solo se debe devolver un resultado de la consulta, incluso
    // si hay mas registros que coinciden con los criterios de busqueda

    @Insert
    suspend fun insertMoney(money: CurrentMoneyEntity)
    //se utiliza para insertar un nuevo registro de dinero actual en la base de datos

    @Update
    suspend fun updateMoney(money: CurrentMoneyEntity)
    //se utiliza para actualizar el registro existente de dinero actual en la base de datos

    @Delete
    suspend fun deleteMoney(money: CurrentMoneyEntity)

    @Query("delete from currentMoneyEntity")
    suspend fun deleteAllMoney()

}