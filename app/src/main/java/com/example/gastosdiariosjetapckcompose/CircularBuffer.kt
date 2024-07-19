package com.example.gastosdiariosjetapckcompose

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.DeleteBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.GetAllBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.InsertBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.UpdateBarGraphUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CircularBuffer(
    private val capacity: Int,
    private val insertBarGraphUseCase: InsertBarGraphUseCase,
    private val updateBarGraphUseCase: UpdateBarGraphUseCase,
    private val getAllBarGraphUseCase: GetAllBarGraphUseCase,
    private val deleteBarGraphUseCase: DeleteBarGraphUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val buffer = mutableListOf<BarDataModel>()


    init {
        // Cargar datos iniciales del caso de uso GetAllBarGraphUseCase
        loadInitialData()
    }

    private fun loadInitialData() {
        coroutineScope.launch{
            try {
                withContext(Dispatchers.IO){
                    getAllBarGraphUseCase().collect { items ->
                        buffer.addAll(items)
                        // Ajustar tamaño del buffer si excede la capacidad
                        if (buffer.size > capacity) {
                            buffer.subList(0, buffer.size - capacity).clear()
                        }
                    }
                }

            } catch (e: Exception) {
                // Manejar errores al cargar datos iniciales
                // Por ejemplo, enviar un mensaje de error o registrar el evento
            }
        }
    }

    fun addBuffer(valueList: List<BarDataModel>) {
        coroutineScope.launch {
            valueList.forEach { item ->
                val existingIndex = buffer.indexOfFirst { it.month == item.month }
                if (existingIndex != -1) {
                    // Actualizar money y value del elemento existente en buffer
                    buffer[existingIndex].value = item.value
                    buffer[existingIndex].money = item.money

                    // Crear un nuevo objeto BarDataModel con los valores actualizados
                    val updatedItem = BarDataModel(
                        id = buffer[existingIndex].id,  // Asegúrate de mantener el mismo ID si es necesario
                        value = item.value,
                        month = buffer[existingIndex].month,
                        money = item.money
                    )
                    updateBarGraph(updatedItem)
                    Log.d("circularBuffer", "Actualizando")
                } else {
                    if (buffer.size >= capacity) {
                        val removedItem = buffer.removeFirst()
                        deleteBarGraph(removedItem)
                        Log.d("circularBuffer", "Borrando")
                    }
                    buffer.add(item)
                    insertBarGraph(item)
                    Log.d("circularBuffer", "Insertando")
                }
            }
        }
    }



    private suspend fun insertBarGraph(item: BarDataModel) {
        coroutineScope.launch { insertBarGraphUseCase(item) }
    }

    private suspend fun updateBarGraph(item: BarDataModel) {
        coroutineScope.launch { updateBarGraphUseCase(item) }
    }

    private suspend fun deleteBarGraph(item: BarDataModel) {
        coroutineScope.launch { deleteBarGraphUseCase(item) }

    }

    fun getBufferList(): List<BarDataModel> {
        return buffer.toList()
    }
}
