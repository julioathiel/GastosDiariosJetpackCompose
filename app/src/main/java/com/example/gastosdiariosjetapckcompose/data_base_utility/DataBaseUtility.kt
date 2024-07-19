package com.example.gastosdiariosjetapckcompose.data_base_utility

import com.example.gastosdiariosjetapckcompose.domain.usecase.money.GetCurrentMoneyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//object DataBaseUtility {
//    suspend fun obtenerDineroTotal(getCurrentMoneyUseCase: GetCurrentMoneyUseCase): Double {
//        return withContext(Dispatchers.IO) {
//            getCurrentMoneyUseCase().money
//        }
//    }
//}