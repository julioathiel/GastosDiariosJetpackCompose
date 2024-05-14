package com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso


import com.example.gastosdiariosjetapckcompose.data.di.repository.UsuarioCreaCatIngresoRepository
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatIngresosModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsertUserCatIngresoUsecase @Inject constructor(private val usuarioCreaCatIngresoRepository: UsuarioCreaCatIngresoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatIngresosModel){
        usuarioCreaCatIngresoRepository.insertUsuarioCreaCatIngresosRepository(item)
    }
}
class UpdateUserCatIngresoUsecase @Inject constructor(private val usuarioCreaCatIngresoRepository: UsuarioCreaCatIngresoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatIngresosModel){
        usuarioCreaCatIngresoRepository.updateUsuarioCreaCatIngresosRepository(item)
    }
}
class GetUserCatIngresoUsecase @Inject constructor(private val usuarioCreaCatIngresoRepository: UsuarioCreaCatIngresoRepository){
    operator fun invoke(): Flow<List<UsuarioCreaCatIngresosModel>> {
        return usuarioCreaCatIngresoRepository.listaCategoriaUnicaIngresos
    }
}
class DeleteUserCatIngresoUsecase @Inject constructor(private val usuarioCreaCatIngresoRepository: UsuarioCreaCatIngresoRepository){
    suspend operator fun invoke(item: UsuarioCreaCatIngresosModel){
        usuarioCreaCatIngresoRepository.deleteUsuarioCreaCatIngresosRepository(item)
    }
}