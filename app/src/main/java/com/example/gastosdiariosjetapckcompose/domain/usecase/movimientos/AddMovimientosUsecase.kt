package com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos

import com.example.gastosdiariosjetapckcompose.data.di.repository.MovimientosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import javax.inject.Inject

class AddMovimientosUsecase @Inject constructor(private val movimientosRepository: MovimientosRepository){
    suspend operator fun invoke(item:MovimientosModel){
        movimientosRepository.insertMovimientosRepository(item)
    }
}