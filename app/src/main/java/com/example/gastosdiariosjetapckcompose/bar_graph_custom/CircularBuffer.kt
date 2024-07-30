package com.example.gastosdiariosjetapckcompose.bar_graph_custom

import android.util.Log
import com.example.gastosdiariosjetapckcompose.domain.model.BarDataModel
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.DeleteBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.GetAllBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.InsertBarGraphUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.bar_graph.UpdateBarGraphUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class CircularBuffer(
    private val capacity: Int,
    private val insertBarGraphUseCase: InsertBarGraphUseCase,
    private val updateBarGraphUseCase: UpdateBarGraphUseCase,
    private val getAllBarGraphUseCase: GetAllBarGraphUseCase,
    private val deleteBarGraphUseCase: DeleteBarGraphUseCase,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
//    private val buffer = mutableListOf<BarDataModel>()
//
//    private fun loadInitialData() {
//        coroutineScope.launch {
//            try {
//                val data = withContext(Dispatchers.IO) {
//                    getAllBarGraphUseCase().collect { items ->
//                        buffer.addAll(items)
//                        // Ajustar tamaño del buffer si excede la capacidad
//                        if (buffer.size > capacity) {
//                            buffer.subList(0, buffer.size - capacity).clear()
//                        }
//                    }
//                }
//                Log.d("circularBuffer", "data= $data")
//
//            } catch (e: Exception) {
//                Log.e("DatosInicialesGraph", "no se pudo cargar los datos ${e.message}")
//            }
//        }
//    }
//
//    fun addBuffer(valueList: List<BarDataModel>) {
//        coroutineScope.launch {
//            valueList.forEach { item ->
//                if(buffer.isEmpty()){
//                    insertBarGraph(item)
//                    Log.d("circularBuffer", "insertadno")
//                }
//                val existingIndex = buffer.indexOfFirst { it.month == item.month }
//                if (existingIndex != -1) {
//                    // Actualizar money y value del elemento existente en buffer
//                    buffer[existingIndex].value = item.value
//                    buffer[existingIndex].money = item.money
//
//                    // Crear un nuevo objeto BarDataModel con los valores actualizados
//                    val updatedItem = BarDataModel(
//                        id = buffer[existingIndex].id,  // Asegúrate de mantener el mismo ID si es necesario
//                        value = item.value,
//                        month = buffer[existingIndex].month,
//                        money = item.money
//                    )
//                    updateBarGraph(updatedItem)
//                    Log.d("circularBuffer", "Actualizando")
//                } else {
//                    if (buffer.size >= capacity) {
//                        val removedItem = buffer.removeFirst()
//                        deleteBarGraph(removedItem)
//                        Log.d("circularBuffer", "Borrando")
//                    }
//                }
//            }
//        }
//    }
//
//
//    private suspend fun insertBarGraph(item: BarDataModel) {
//        coroutineScope.launch { insertBarGraphUseCase(item) }
//    }
//
//    private suspend fun updateBarGraph(item: BarDataModel) {
//        coroutineScope.launch { updateBarGraphUseCase(item) }
//    }
//
//    private suspend fun deleteBarGraph(item: BarDataModel) {
//        coroutineScope.launch { deleteBarGraphUseCase(item) }
//
//    }
//
//    fun getBufferList(): List<BarDataModel> {
//        return buffer.toList()
//    }


    private suspend fun adjustBufferCapacityIfNeeded() {
        coroutineScope.launch {
            val currentSizeFlow: Flow<List<BarDataModel>> = getAllBarGraphUseCase()

            currentSizeFlow.collect { currentSize ->
                val currentSizeInt = currentSize.size
                if (currentSizeInt > capacity) {
                    val itemsToRemove = currentSizeInt - capacity
                    val itemsToRemoveList = currentSize.subList(0, itemsToRemove)
                    itemsToRemoveList.forEach { item ->
                        deleteBarGraph(item)
                    }
                }
            }
        }

    }

//    fun addBarGraph(item: BarDataModel) {
//        coroutineScope.launch {
//            adjustBufferCapacityIfNeeded()
//            getAllBarGraphUseCase()
//                .collect { barGraphList ->
//                val existingItem = barGraphList.find { it.month == item.month }
//                if (existingItem != null) {
//                    val updatedItem = existingItem.copy(value = item.value, money = item.money)
//                    updateBarGraph(updatedItem)
//                    Log.d("circularBuffer", "Item actualizado: $updatedItem")
//                } else {
//                    insertBarGraph(item)
//                    Log.d("circularBuffer", "Nuevo item insertado: $item")
//                }
//            }
//        }
//    }

    fun addBarGraph(item: BarDataModel) {
        coroutineScope.launch {
            adjustBufferCapacityIfNeeded()

            val barGraphList = getAllBarGraphUseCase().first()
            val existingItem = barGraphList.find { it.month == item.month }

            if(existingItem != null){
                val updatedItem = existingItem.copy(value = item.value, money = item.money)
                updateBarGraph(updatedItem)
                Log.d("circularBuffer", "Item actualizado: $updatedItem")
            } else {
                insertBarGraph(item)
                Log.d("circularBuffer", "Item insertado: $item")
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

    fun getBarGraphList(): Flow<List<BarDataModel>> {
        return getAllBarGraphUseCase()
    }
}
