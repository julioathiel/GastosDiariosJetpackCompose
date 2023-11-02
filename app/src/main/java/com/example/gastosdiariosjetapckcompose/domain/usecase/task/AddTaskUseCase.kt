package com.example.gastosdiariosjetapckcompose.domain.usecase.task

import com.example.gastosdiariosjetapckcompose.data.di.repository.TaskRepository
import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository){
    //esta funcion recibira un modelo de la ui que nos dice que inserte esto,
    // es por eso que se le pasa un TaskModel
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.add(taskModel)
    }
}