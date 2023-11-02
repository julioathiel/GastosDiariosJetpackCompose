package com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida

import com.example.gastosdiariosjetapckcompose.data.di.repository.FechaSaveRepository
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import javax.inject.Inject

class GetFechaUsecase @Inject constructor(private val repository: FechaSaveRepository) {
   suspend operator fun invoke(): FechaSaveModel{
        return repository.getFechaElegida()
    }
}