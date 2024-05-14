package com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos

import com.example.gastosdiariosjetapckcompose.data.di.repository.MovimientosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddMovimientosUsecase @Inject constructor(private val movimientosRepository: MovimientosRepository){
    suspend operator fun invoke(item: MovimientosModel){
        movimientosRepository.insertMovimientosRepository(item)
    }
}
class UpdateMovimientosUsecase @Inject constructor(private val movimientosRepository: MovimientosRepository){
    suspend operator fun invoke(item:MovimientosModel){
        movimientosRepository.updateMovimientosRepository(item)
    }
}

class GetMovimientosUsecase @Inject constructor(private val movimientosRepository: MovimientosRepository){
    operator fun invoke(): Flow<List<MovimientosModel>> {
        return movimientosRepository.movimientos
    }
}

class DeleteMovimientosUsecase @Inject constructor(private val movimientosRepository: MovimientosRepository) {
    suspend operator fun invoke(item: MovimientosModel) {
        movimientosRepository.deleteMovimientosRepository(item)
    }
}

class CheckDatabaseAllExpensesEmptyUseCase @Inject constructor(private val movimientosRepository: MovimientosRepository) {
    suspend operator fun invoke(): Boolean {
        return movimientosRepository.isDatabaseAllExpensesEmpty()
    }
}