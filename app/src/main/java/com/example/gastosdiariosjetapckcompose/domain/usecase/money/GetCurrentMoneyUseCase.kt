package com.example.gastosdiariosjetapckcompose.domain.usecase.money

import com.example.gastosdiariosjetapckcompose.data.di.repository.CurrentMoneyRepository
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentMoneyUseCase @Inject constructor(private val currentMoneyRepository: CurrentMoneyRepository) {
    suspend operator fun invoke(): CurrentMoneyModel{
        return currentMoneyRepository.getMoney()
    }
}