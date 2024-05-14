package com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.TotalGastosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import javax.inject.Inject

class InsertTotalGastosUseCase @Inject constructor(private val totalGastosRepository: TotalGastosRepository) {
    suspend operator fun invoke(item:TotalGastosModel) {
        totalGastosRepository.insertTotalGastos(item)
    }
}

class UpdateTotalGastosUseCase @Inject constructor(private val totalGastosRepository: TotalGastosRepository) {
    suspend operator fun invoke(item:TotalGastosModel) {
        totalGastosRepository.updateTotalGastos(item)
    }
}

class GetTotalGastosUseCase @Inject constructor(private val totalGastosRepository: TotalGastosRepository) {
    suspend operator fun invoke(): TotalGastosModel? {
        return totalGastosRepository.getTotalGastos()
    }
}

class DeleteTotalGastosUseCase @Inject constructor(private val totalGastosRepository: TotalGastosRepository) {
    suspend operator fun invoke(item:TotalGastosModel) {
        return totalGastosRepository.deleteTotalGastos(item)
    }
}
class CheckDatabaseTotalGastosEmptyUseCase @Inject constructor(private val totalGastosRepository: TotalGastosRepository) {
    suspend operator fun invoke(): Boolean {
        return totalGastosRepository.isDatabaseTotalGastosEmpty()
    }
}





