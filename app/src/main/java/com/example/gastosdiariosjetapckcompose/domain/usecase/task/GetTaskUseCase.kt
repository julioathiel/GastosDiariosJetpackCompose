package com.example.gastosdiariosjetapckcompose.domain.usecase.task

import com.example.gastosdiariosjetapckcompose.data.di.repository.TaskRepository
import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//necesitamos injectar el repositorio ya que es como
// la puerta de entrada a data asi que por parametro se lo pasamos
class GetTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    //este caso de uso, esta clase nos devolvera un flow,
// es por eso que ala funcion le pasamos que nos retorne un flow que contiene una lista de TaskModel
    operator fun invoke(): Flow<List<TaskModel>> {
        //retornamos el taskRepository y le pasamos la variable tasks,
        //de esta manera ya se estaria haciendo el caso de uso
        return taskRepository.tasks
    }
    //este caso de uso solo recupera las tareas
}