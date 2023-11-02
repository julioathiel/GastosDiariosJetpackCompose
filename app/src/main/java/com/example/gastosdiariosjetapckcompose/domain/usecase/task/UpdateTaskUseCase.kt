package com.example.gastosdiariosjetapckcompose.domain.usecase.task

import com.example.gastosdiariosjetapckcompose.data.di.repository.TaskRepository
import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.update(taskModel)
    }
}