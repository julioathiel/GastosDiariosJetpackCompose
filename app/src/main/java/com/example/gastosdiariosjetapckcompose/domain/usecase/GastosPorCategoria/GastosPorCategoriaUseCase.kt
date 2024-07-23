package com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorCategoria

import com.example.gastosdiariosjetapckcompose.data.di.repository.FechaSaveRepository
import com.example.gastosdiariosjetapckcompose.data.di.repository.GastosPorCategoriaRepository
import com.example.gastosdiariosjetapckcompose.domain.model.GastosPorCategoriaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class InsertGastosPorCategoriaUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository) {
    suspend operator fun invoke(item:GastosPorCategoriaModel){
        gastosPorCategoriaRepository.insertGastosPorCategoria(item)
    }
}

class UpdateGastosPorCategoriaUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository) {
    suspend operator fun invoke(nombre: String,cantidadIngresada:Double) {
        gastosPorCategoriaRepository.actualizarCategoriaIndividualRepository(nombre, cantidadIngresada)
    }
}

class UpdateItemGastosPorCategoriaUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository){
    suspend operator fun invoke(item: GastosPorCategoriaModel){
        gastosPorCategoriaRepository.updateGastosPorCategoriaRepository(item)
    }
}

class GetGastosPorCategoriaUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository) {
    operator fun invoke(): Flow<List<GastosPorCategoriaModel>> {
        return gastosPorCategoriaRepository.categoriaUnica
    }
}
class DeleteGastosPorCategoriaUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository) {
    suspend operator fun invoke(item: GastosPorCategoriaModel) {
        gastosPorCategoriaRepository.deleteGastosPorCategoriaRepository(item)
    }

}

class CheckDatabaseGastosPorCategoriaEmptyUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository){
    suspend operator fun invoke(): Boolean {
        return gastosPorCategoriaRepository.isDatabaseGastosPorCategoriaEmpty()
    }
}

class ClearAllGastosPorCatUseCase @Inject constructor(private val gastosPorCategoriaRepository: GastosPorCategoriaRepository){
    suspend operator fun invoke(){
        gastosPorCategoriaRepository.clearAllGastosPorCat()
    }
}