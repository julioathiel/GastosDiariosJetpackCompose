package com.example.gastosdiariosjetapckcompose.domain.usecase.imagenSeleccionada

import com.example.gastosdiariosjetapckcompose.data.di.repository.UriImagenSeleccionadaRepository
import com.example.gastosdiariosjetapckcompose.domain.model.ImagenSeleccionadaModel
import javax.inject.Inject

class AddUriImagenSeleccionadaUseCase @Inject constructor(private val repository: UriImagenSeleccionadaRepository){
    suspend operator fun invoke(imagenSeleccionadaModel: ImagenSeleccionadaModel){
        repository.insertUriImagenSeleccionada(imagenSeleccionadaModel)
    }
}

class UpdateUriImagenSeleccionadaUseCase @Inject constructor(private val repository:UriImagenSeleccionadaRepository){
    suspend operator fun invoke(imagenSeleccionadaModel: ImagenSeleccionadaModel){
        return repository.updateImagenSeleccionada(imagenSeleccionadaModel)
    }
}


class GetUriImagenSeleccionadaUseCase @Inject constructor(private val repository:UriImagenSeleccionadaRepository) {
    suspend operator fun invoke():ImagenSeleccionadaModel{
        return repository.getImagenGuardada()
    }
}

class DeleteUriImagenSeleccionadaUseCase @Inject constructor(private val repository: UriImagenSeleccionadaRepository) {
    suspend operator fun invoke(imagenSeleccionadaModel: ImagenSeleccionadaModel) {
        repository.deleteUriImagenSeleccionada(imagenSeleccionadaModel)
    }
}

class ClearAllImagenSelectedUseCase @Inject constructor(private val repository: UriImagenSeleccionadaRepository){
    suspend operator fun invoke(){
        repository.deleteAlImagenSelectedRepository()
    }
}