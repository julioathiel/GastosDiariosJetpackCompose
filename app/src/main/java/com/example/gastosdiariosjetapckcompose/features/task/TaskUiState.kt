package com.example.gastosdiariosjetapckcompose.features.task

import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel

sealed interface TaskUiState{
    //si no se recibe datos, entonces creamos un object
    object Loading: TaskUiState
    //en caso de fallar la carga se envia un error
    data class Error(val throwable: Throwable): TaskUiState
    //EL SUCCESS es cuando el estado de nuestra pantalla es
// correcto, cuando tenemos datos y no hay errores, tenemos una
// pantalla que queremos mostrar, pues era con el estado Success
    data class  Success(val tasks:List<TaskModel>): TaskUiState
}