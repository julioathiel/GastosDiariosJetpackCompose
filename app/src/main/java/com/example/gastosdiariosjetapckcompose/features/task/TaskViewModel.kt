package com.example.gastosdiariosjetapckcompose.features.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.domain.usecase.task.AddTaskUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.task.DeleteTaskUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.task.GetTaskUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.task.UpdateTaskUseCase
import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel
import com.example.gastosdiariosjetapckcompose.features.task.TaskUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTaskUseCase: GetTaskUseCase
) : ViewModel() {
    //si toto va bien, agarramos todoo el listado,
    // y por cada uno de los items, lo metemos en Success
    val uiState: StateFlow<TaskUiState> = getTaskUseCase().map(::Success)
        //si llega a fallar
        .catch { TaskUiState.Error(it) }
        //stateIn convierte un Flow en un StateFlow
        //SharingStarted.WhileSubscribed(5000) con eso le decimos que cuando la pantalla este en
        // segundo plano y si pasaron 5 segundos que no volviste, que pare el flow
        //con Loding le decimos que el estado inicial es Loading
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), TaskUiState.Loading
        )

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    //funcion para cerrar el dialogo
    fun onDialogClose() {
        //al estar en false, se cierra el dialogo
        _showDialog.value = false
    }

    fun onTaskCreate(task: String) {
        //se cierra el dialogo
        _showDialog.value = false
        //se agrega una tarea de tipo TaskModel
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    // mostrando dialog
    fun onShowDialogClick() {
        _showDialog.value = true
    }

    //seleccionando checkBox
    fun onCheckBoxSelected(taskModel: TaskModel) {
     viewModelScope.launch {
         updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))
     }
    }

    //eliminando checkBox
    fun onItemRemove(taskModel: TaskModel) {
        // le estamos diciendo que nos busque en la tarea (it) un id que sea igual a taskModel.id
        //ya que en este caso el id es unico
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }
}