package com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph

import com.example.gastosdiariosjetapckcompose.data.di.repository.BarDataGraphRepository
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class InsertBarGraphUseCase @Inject constructor(private val repository: BarDataGraphRepository) {
    suspend operator fun invoke(item: BarDataModel) {
        repository.insertBarDataGraphRepo(item)
    }
}

class GetAllBarGraphUseCase @Inject constructor(private val repository: BarDataGraphRepository) {
   operator fun invoke(): Flow<List<BarDataModel>> {
        return repository.getAllBarDataGraphRepo
    }
}

class UpdateBarGraphUseCase @Inject constructor(private val repository: BarDataGraphRepository) {
    suspend operator fun invoke(item: BarDataModel){
        repository.updateBarDataGraphRepo(item)
    }
}

class DeleteBarGraphUseCase @Inject constructor(private val repository: BarDataGraphRepository) {
    suspend operator fun invoke(item: BarDataModel) {
        repository.deleteBarDataGraphRepo(item)
    }
}

class ClearAllBarDataGraphUseCase @Inject constructor(private val repository: BarDataGraphRepository){
    suspend operator fun invoke(){
        repository.clearBarData()
    }
}

