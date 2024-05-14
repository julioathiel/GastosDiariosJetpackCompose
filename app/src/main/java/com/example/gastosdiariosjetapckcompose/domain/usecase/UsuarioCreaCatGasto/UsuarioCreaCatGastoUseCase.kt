package com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto

import com.example.gastosdiariosjetapckcompose.data.di.repository.UsuarioCreaCatGastoRepository
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertUserCatGastoUsecase @Inject constructor(private val usuarioCreaCatGastoRepository: UsuarioCreaCatGastoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatGastoModel){
        usuarioCreaCatGastoRepository.insertUsuarioCreaCatGastoRepository(item)
    }
}
class UpdateUserCatGastoUsecase @Inject constructor(private val usuarioCreaCatGastoRepository: UsuarioCreaCatGastoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatGastoModel){
        usuarioCreaCatGastoRepository.updateUsuarioCreaCatGastoRepository(item)
    }
}
class GetUserCatGastoUsecase @Inject constructor(private val usuarioCreaCatGastoRepository: UsuarioCreaCatGastoRepository){
    operator fun invoke(): Flow<List<UsuarioCreaCatGastoModel>> {
        return usuarioCreaCatGastoRepository.listaCategoriaUnicaGastos
    }
}
class DeleteUserCatGastoUsecase @Inject constructor(private val usuarioCreaCatGastoRepository: UsuarioCreaCatGastoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatGastoModel){
        usuarioCreaCatGastoRepository.deleteUsuarioCreaCatGastoRepository(item)
    }
}