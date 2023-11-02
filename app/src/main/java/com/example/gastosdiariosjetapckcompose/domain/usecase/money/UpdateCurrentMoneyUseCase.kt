package com.example.gastosdiariosjetapckcompose.domain.usecase.money

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import javax.inject.Inject

class UpdateCurrentMoneyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(money: CurrentMoneyModel){
        currentMoneyRepository.updateCurrentMoney(money)
    }
}