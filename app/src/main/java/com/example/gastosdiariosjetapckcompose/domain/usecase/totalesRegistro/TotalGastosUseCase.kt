package com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.TotalGastosRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.TotalIngresosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import javax.inject.Inject

class InsertTotalGastosUseCase @Inject constructor(private val repository: TotalGastosRepository) {
    suspend operator fun invoke(item:TotalGastosModel) {
        repository.insertTotalGastos(item)
    }
}

class UpdateTotalGastosUseCase @Inject constructor(private val repository: TotalGastosRepository) {
    suspend operator fun invoke(item:TotalGastosModel) {
        repository.updateTotalGastos(item)
    }
}

class GetTotalGastosUseCase @Inject constructor(private val repository: TotalGastosRepository) {
    suspend operator fun invoke(): TotalGastosModel? {
        return repository.getTotalGastos()
    }
}

class DeleteTotalGastosUseCase @Inject constructor(private val repository: TotalGastosRepository) {
    suspend operator fun invoke(item:TotalGastosModel) {
        return repository.deleteTotalGastos(item)
    }
}

class ClearAllTotalGastosUseCase @Inject constructor(private val repository: TotalGastosRepository){
    suspend operator fun invoke(){
        repository.clearAllTotalGastosRepository()
    }
}





