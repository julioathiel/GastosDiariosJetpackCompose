package com.example.gastosdiariosjetapckcompose.features.creandoCategoriaIngresos

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.DeleteUserCatIngresoUsecase
import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.GetUserCatIngresoUsecase
import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.InsertUserCatIngresoUsecase
import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.UpdateUserCatIngresoUsecase
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryCrear
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryGasto
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryIngreso
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatIngresosModel
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos
import com.example.gastosdiariosjetapckcompose.domain.uiState.CatIngresosUiState
import com.example.gastosdiariosjetapckcompose.domain.uiState.ResultUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaIngresosViewModel @Inject constructor(
    private val insertUserCatIngresoUsecase: InsertUserCatIngresoUsecase,
    private val updateUserCatIngresoUsecase: UpdateUserCatIngresoUsecase,
    private val getUserCatIngresoUsecase: GetUserCatIngresoUsecase,
    private val deleteUserCatIngresoUsecase: DeleteUserCatIngresoUsecase,
) : ViewModel() {

    val uiState: StateFlow<CatIngresosUiState> =
        getUserCatIngresoUsecase().map(CatIngresosUiState::Success)
            .catch { CatIngresosUiState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                CatIngresosUiState.Loading
            )

    private val _isActivated = mutableStateOf(false)
    val isActivated: State<Boolean> = _isActivated

    private val _idDelElemento = MutableLiveData<Int>()
    val idDelElemento: LiveData<Int> = _idDelElemento

    private val _tituloBottomSheet = MutableStateFlow("")
    val tituloBottomSheet: StateFlow<String> = _tituloBottomSheet.asStateFlow()


    private val _selectedCategoryIngresos = MutableStateFlow<CategoryCrear?>(null)
    val selectedCategoryIngresos: StateFlow<CategoryCrear?> = _selectedCategoryIngresos.asStateFlow()

    private val _isEditarSeleccion = mutableStateOf(false)
    val isEditarSeleccion: State<Boolean> = _isEditarSeleccion

    private val _onDismiss = mutableStateOf(false)
    val onDismiss: State<Boolean> = _onDismiss

    fun isActivatedTrue() {
        _isActivated.value = true
    }

    fun isActivatedFalse() {
        _isActivated.value = false
    }
    fun onDismissSet(value: Boolean) {
        _onDismiss.value = value
    }
    fun iconoSelecionadoIngresos(iconoSeleccionado: CategoryCrear) {
        _selectedCategoryIngresos.value = iconoSeleccionado
    }

    fun actualizarTituloIngresos(it: String) {
        _tituloBottomSheet.value = it
    }
    fun crearNuevaCategoriaDeIngresos(itemModel:UsuarioCreaCatIngresosModel) {
        viewModelScope.launch {
            insertUserCatIngresoUsecase(
                UsuarioCreaCatIngresosModel(
                    nombreCategoria = itemModel.nombreCategoria,
                    categoriaIcon = itemModel.categoriaIcon
                )
            )
            isActivatedTrue()
        }
    }


    fun editarItemSelected(itemModel: UsuarioCreaCatIngresosModel, iconoSeleccionado: Int) {
        viewModelScope.launch {
            _idDelElemento.value = itemModel.id // para compartirlo con la otra funcion
            _tituloBottomSheet.value = itemModel.nombreCategoria
            _selectedCategoryIngresos.value = CategoryCrear(name = "", iconoSeleccionado)
            _isEditarSeleccion.value = true //activa para actualizar al momento de guardar
            _onDismiss.value = true //abre el bottomSheet
        }
    }

    fun eliminarItemSelected(
        item: UsuarioCreaCatIngresosModel
    ) {
        viewModelScope.launch {
            deleteUserCatIngresoUsecase(item)

            val categoriaEliminar = CategoryIngreso(
                item.nombreCategoria,
                item.categoriaIcon.toInt()
            )

            categoriesIngresos.removeAll { it == categoriaEliminar }
        }
    }

    fun borrandoLista() {
        viewModelScope.launch {
            val transaccionesFlow: Flow<List<UsuarioCreaCatIngresosModel>> =
                getUserCatIngresoUsecase()
            val items = transaccionesFlow.first()
            // Elimina todas las categorias de la base de datos
            for (item in items) {
                deleteUserCatIngresoUsecase(item)
                val porElUsuario = CategoryIngreso(item.nombreCategoria, item.categoriaIcon.toInt())
                //se eliminan todos los registros que se crearon asi se actualiza
                categoriesIngresos.removeAll { it == porElUsuario }
            }
        }
    }

    fun actualizandoItemIngresos(itemModel: UsuarioCreaCatIngresosModel) {
        viewModelScope.launch {
            getUserCatIngresoUsecase().map { lista ->
                val movimientoActualizado:UsuarioCreaCatIngresosModel? = lista.map { itemViejo ->
                    if (itemViejo.id == _idDelElemento.value) {
                        //cambiandoles los valores a los items
                        itemViejo.nombreCategoria = itemModel.nombreCategoria
                        itemViejo.categoriaIcon = itemModel.categoriaIcon
                    }
                    itemViejo  // Devolver el movimiento actualizado o sin cambios
                }.find { it.id == _idDelElemento.value } // Encontrar el movimiento actualizado
                movimientoActualizado // Devolver el movimiento actualizado

            }.collect { movimientoActualizado ->
                if (movimientoActualizado != null) {
                    // Si se encontró y actualizó el movimiento, se pasa a la función de actualización individual
                    updateUserCatIngresoUsecase(movimientoActualizado)
                    limpiandoCampoBottomSheetIngresos()
                } else {
                    // Manejar el caso en el que no se encuentre el elemento a actualizar
                    // Puede ser útil lanzar una excepción o manejarlo de alguna otra manera
                }
            }
        }
    }
    private fun limpiandoCampoBottomSheetIngresos() {
        _tituloBottomSheet.value = "" //limpia textField
        _selectedCategoryIngresos.value = null //limpia seleccion
        _isEditarSeleccion.value = false //desactiva para no editar
    }
}