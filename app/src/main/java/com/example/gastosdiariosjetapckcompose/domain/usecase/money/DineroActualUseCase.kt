package com.example.gastosdiariosjetapckcompose.domain.usecase.money

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.MovimientosRepository
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import javax.inject.Inject

class AddCurrentMoneyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(currentMoneyModel: CurrentMoneyModel) {
        currentMoneyRepository.insertCurrentMoney(currentMoneyModel)
    }
}

class GetCurrentMoneyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(): CurrentMoneyModel{
        return currentMoneyRepository.getMoney()
    }
}

class UpdateCurrentMoneyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(money: CurrentMoneyModel){
        currentMoneyRepository.updateCurrentMoney(money)
    }
}

class DeleteCurrentMoneyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(item: CurrentMoneyModel) {
        currentMoneyRepository.deleteCurrentMoneyRepository(item)
    }
}

class CheckDatabaseMoneyEmptyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(): Boolean {
        return currentMoneyRepository.isDatabaseMoneyEmpty()
    }
}