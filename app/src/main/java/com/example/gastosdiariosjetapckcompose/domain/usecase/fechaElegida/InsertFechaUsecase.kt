package com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida

import com.example.gastosdiariosjetapckcompose.data.di.repository.FechaSaveRepository
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import javax.inject.Inject

class InsertFechaUsecase @Inject constructor(private val repository: FechaSaveRepository){
    suspend operator fun invoke(item:FechaSaveModel){
        repository.insertFechaElegida(item)
    }
}