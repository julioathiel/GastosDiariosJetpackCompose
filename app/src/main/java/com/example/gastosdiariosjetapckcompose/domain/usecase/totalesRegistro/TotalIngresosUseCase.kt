package com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.TotalIngresosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalIngresosModel
import javax.inject.Inject

class InsertTotalIngresosUseCase @Inject constructor(private val totalIngresosRepository: TotalIngresosRepository) {
    suspend operator fun invoke(item:TotalIngresosModel) {
        totalIngresosRepository.insertTotalIngresos(item)
    }
}

class UpdateTotalIngresosUseCase @Inject constructor(private val totalIngresosRepository: TotalIngresosRepository) {
    suspend operator fun invoke(item: TotalIngresosModel) {
        totalIngresosRepository.updateTotalIngresos(item)
    }
}

class GetTotalIngresosUseCase @Inject constructor(private val totalIngresosRepository: TotalIngresosRepository) {
    suspend operator fun invoke(): TotalIngresosModel? {
        return totalIngresosRepository.getTotalIngresos()
    }
}

class DeleteTotalIngresosUseCase @Inject constructor(private val totalIngresosRepository:TotalIngresosRepository){
    suspend operator fun invoke(item: TotalIngresosModel) {
        totalIngresosRepository.deleteTotalIngresosRepository(item)
    }
}

class CheckDatabaseTotalIngresosEmptyUseCase @Inject constructor(private val totalIngresosRepository:TotalIngresosRepository) {
    suspend operator fun invoke(): Boolean {
        return totalIngresosRepository.isDatabaseTotalIngresosEmpty()
    }
}
