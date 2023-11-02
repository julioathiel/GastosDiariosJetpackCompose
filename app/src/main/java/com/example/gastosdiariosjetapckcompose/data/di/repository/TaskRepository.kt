package com.example.gastosdiariosjetapckcompose.data.di.repository

import com.example.gastosdiariosjetapckcompose.data.di.dao.TaskDao
import com.example.gastosdiariosjetapckcompose.data.di.entity.TaskEntity
import com.example.gastosdiariosjetapckcompose.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    suspend fun add(taskModel: TaskModel) {
        //le pasamos nuestra entidad, de esta forma añadimos
        taskDao.addTask(TaskEntity(taskModel.id, taskModel.task, taskModel.selected))
    }

    //usamos flow para leer ya que es como un livedata es continua, esta siempre escuchando
    //creamos una variable de un flujo de una lista de TaskModel,
    // ya que es lo que nos devuelve el Dao en el getTask()
    //lo que llamara a la variable tasks sera el dominio (domain),
    //hacemos el map para recibir el dato y lo devolvemos transformado para cada una de las capas
    //nos devolvera unos items
    val tasks: Flow<List<TaskModel>> = taskDao.getTask().map { items ->
        //por cada uno de esos items, le hacemos otro map
        items.map {
            //ahora por cada una de las iteraciones, creamos un model de datos
            TaskModel(it.id, it.task, it.select)
        }
    }
    //quien llame a la variable tasks siempre recibira un flow que contiene una lista
// de TaskModel y nadie sabra que existe TaskEntity salvo la capa de data

    suspend fun update(taskModel: TaskModel){
        taskDao.updateTask(taskModel.toData())
    }

    suspend fun delete(taskModel: TaskModel){
        taskDao.deleteTask(taskModel.toData())
    }
}
//creando extension pra no repetir TaskEntity(taskModel.id, taskModel.task, taskModel.selected)
fun TaskModel.toData(): TaskEntity {
    return TaskEntity(this.id, this.task,this.selected)
}