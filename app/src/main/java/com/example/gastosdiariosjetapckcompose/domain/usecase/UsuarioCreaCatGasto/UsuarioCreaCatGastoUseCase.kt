package com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto

import com.example.gastosdiariosjetapckcompose.data.di.repository.UsuarioCreaCatGastoRepository
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertUserCatGastoUsecase @Inject constructor(private val repository: UsuarioCreaCatGastoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatGastoModel){
        repository.insertUsuarioCreaCatGastoRepository(item)
    }
}
class UpdateUserCatGastoUsecase @Inject constructor(private val repository: UsuarioCreaCatGastoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatGastoModel){
        repository.updateUsuarioCreaCatGastoRepository(item)
    }
}
class GetUserCatGastoUsecase @Inject constructor(private val repository: UsuarioCreaCatGastoRepository){
    operator fun invoke(): Flow<List<UsuarioCreaCatGastoModel>> {
        return repository.listaCategoriaUnicaGastos
    }
}
class DeleteUserCatGastoUsecase @Inject constructor(private val repository: UsuarioCreaCatGastoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatGastoModel){
        repository.deleteUsuarioCreaCatGastoRepository(item)
    }
}

class ClearAllUserCatGastosUseCase @Inject constructor(private val repository : UsuarioCreaCatGastoRepository){
    suspend operator fun  invoke(){
        repository.clearAllUserCatGastosRepository()
    }
}