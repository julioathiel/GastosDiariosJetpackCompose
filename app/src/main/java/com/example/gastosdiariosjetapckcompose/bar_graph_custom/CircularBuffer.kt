package com.example.gastosdiariosjetapckcompose.bar_graph_custom

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


    fun addBarGraph(item: BarDataModel) {
        coroutineScope.launch {
            adjustBufferCapacityIfNeeded()

            val barGraphList = getAllBarGraphUseCase().first()
            val existingItem = barGraphList.find { it.month == item.month }

            if(existingItem != null){
                val updatedItem = existingItem.copy(value = item.value, money = item.money)
                updateBarGraph(updatedItem)
            } else {
                insertBarGraph(item)
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
