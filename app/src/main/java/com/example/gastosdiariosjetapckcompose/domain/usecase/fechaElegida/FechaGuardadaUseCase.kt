package com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.FechaSaveRepository
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import javax.inject.Inject

class InsertFechaUsecase @Inject constructor(private val repository: FechaSaveRepository){
    suspend operator fun invoke(item: FechaSaveModel){
        repository.insertFechaElegida(item)
    }
}

class UpdateFechaUsecase @Inject constructor(private val repository: FechaSaveRepository){
    suspend operator fun invoke(item:FechaSaveModel){
        repository.updateFechaElegida(item)
    }
}

class GetFechaUsecase @Inject constructor(private val repository: FechaSaveRepository) {
    suspend operator fun invoke(): FechaSaveModel? {
        return repository.getFechaElegida()
    }
}

class DeleteFechaUsecase @Inject constructor(private val repository: FechaSaveRepository){
    suspend operator fun invoke(item: FechaSaveModel){
        repository.deleteFechaElegida(item)
    }
}


class CheckDatabaseFechaGuardadaEmptyUseCase @Inject constructor(private val repository: FechaSaveRepository) {
    suspend operator fun invoke(): Boolean {
        return repository.isDatabaseFechaGuardadaEmpty()
    }
}

