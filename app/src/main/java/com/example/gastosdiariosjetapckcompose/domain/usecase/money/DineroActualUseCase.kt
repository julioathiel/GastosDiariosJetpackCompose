package com.example.gastosdiariosjetapckcompose.domain.usecase.money

import com.example.gastosdiariosjetapckcompose.data.di.entity.CurrentMoneyEntity
import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import javax.inject.Inject

class AddCurrentMoneyUseCase @Inject constructor(private val repository: CurrentMoneyRepository) {
    suspend operator fun invoke(currentMoneyModel: CurrentMoneyModel) {
        repository.insertCurrentMoney(currentMoneyModel)
    }
}

class GetCurrentMoneyUseCase @Inject constructor(private val repository: CurrentMoneyRepository) {
    suspend operator fun invoke(): CurrentMoneyModel{
        return repository.getMoney()
    }
}

class UpdateCurrentMoneyUseCase @Inject constructor(private val repository: CurrentMoneyRepository) {
    suspend operator fun invoke(money: CurrentMoneyModel){
        repository.updateCurrentMoney(money)
    }
}

class DeleteCurrentMoneyUseCase @Inject constructor(private val repository: CurrentMoneyRepository) {
    suspend operator fun invoke(item: CurrentMoneyModel) {
        repository.deleteCurrentMoneyRepository(item)
    }
}

class ClearAllCurrentMoneyUseCase @Inject constructor(private val repository: CurrentMoneyRepository){
    suspend operator fun invoke(){
        repository.clearAllCurrentMoneyRepository()
    }
}