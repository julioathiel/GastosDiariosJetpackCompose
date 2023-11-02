package com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos

import com.example.gastosdiariosjetapckcompose.data.di.repository.MovimientosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovimientosUsecase @Inject constructor(private val movimientosRepository: MovimientosRepository){
    operator fun invoke(): Flow<List<MovimientosModel>> {
        return movimientosRepository.movimientos
    }
}