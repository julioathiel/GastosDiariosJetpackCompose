package com.example.gastosdiariosjetapckcompose.domain.usecase.imagenSeleccionada

import com.example.gastosdiariosjetapckcompose.data.di.repository.UriImagenSeleccionadaRepository
import com.example.gastosdiariosjetapckcompose.domain.model.ImagenSeleccionadaModel
import javax.inject.Inject

class GetUriImagenSeleccionadaUseCase @Inject constructor(private val repository:UriImagenSeleccionadaRepository) {
    suspend operator fun invoke():ImagenSeleccionadaModel{
       return repository.getImagenGuardada()
    }
}