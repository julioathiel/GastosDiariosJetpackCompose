package com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.TotalIngresosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalIngresosModel
import javax.inject.Inject

class InsertTotalIngresosUseCase @Inject constructor(private val repository: TotalIngresosRepository) {
    suspend operator fun invoke(item:TotalIngresosModel) {
        repository.insertTotalIngresos(item)
    }
}

class UpdateTotalIngresosUseCase @Inject constructor(private val repository: TotalIngresosRepository) {
    suspend operator fun invoke(item: TotalIngresosModel) {
        repository.updateTotalIngresos(item)
    }
}

class GetTotalIngresosUseCase @Inject constructor(private val repository: TotalIngresosRepository) {
    suspend operator fun invoke(): TotalIngresosModel? {
        return repository.getTotalIngresos()
    }
}

class DeleteTotalIngresosUseCase @Inject constructor(private val repository:TotalIngresosRepository){
    suspend operator fun invoke(item: TotalIngresosModel) {
        repository.deleteTotalIngresosRepository(item)
    }
}

class CheckDatabaseTotalIngresosEmptyUseCase @Inject constructor(private val repository:TotalIngresosRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isDatabaseTotalIngresosEmpty()
    }
}

class ClearAllTotalIngresosUseCase @Inject constructor(private val repository: TotalIngresosRepository){
    suspend operator fun invoke(){
        repository.clearAllTotalIngresosRepository()
    }
}
