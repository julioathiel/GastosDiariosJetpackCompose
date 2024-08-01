package com.example.gastosdiariosjetapckcompose.mis_ui_screen.creandoCategoriaGastos

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.data.di.dao.UsuarioCreaCatGastoDao
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryCrear
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryGasto
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.uiState.ResultUiState
import com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto.DeleteUserCatGastoUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto.GetUserCatGastoUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto.InsertUserCatGastoUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto.UpdateUserCatGastoUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
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
class CategoriaGastosViewModel @Inject constructor(
    private val insertUserCatGastoUsecase: InsertUserCatGastoUsecase,
    private val getUserCatGastoUsecase: GetUserCatGastoUsecase,
    private val updateUserCatGastoUsecase: UpdateUserCatGastoUsecase,
    private val deleteUserCatGastoUsecase: DeleteUserCatGastoUsecase,
    private val dao: UsuarioCreaCatGastoDao
) : ViewModel() {
    val uiState: StateFlow<ResultUiState> =
        getUserCatGastoUsecase().map(ResultUiState::Success)
            .catch { ResultUiState.Error(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ResultUiState.Loading
            )


    private val _tituloBottomSheet = MutableStateFlow("")
    val tituloBottomSheet: StateFlow<String> = _tituloBottomSheet.asStateFlow()

    private val _onDismiss = mutableStateOf(false)
    val onDismiss: State<Boolean> = _onDismiss

    private val _selectedCategoryGastos = MutableStateFlow<CategoryCrear?>(null)
    val selectedCategoryGastos: StateFlow<CategoryCrear?> = _selectedCategoryGastos.asStateFlow()

    private val _isActivated = mutableStateOf(false)
    val isActivated: State<Boolean> = _isActivated

    private val _isEditarSeleccion = mutableStateOf(false)
    val isEditarSeleccion: State<Boolean> = _isEditarSeleccion


    private val _idDelElemento = MutableLiveData<Int>()
    val idDelElemento: LiveData<Int> = _idDelElemento


    fun isActivatedTrue() {
        _isActivated.value = true
    }
    fun isActivatedFalse() {
        _isActivated.value = false
    }
    fun onDismissSet(value: Boolean) {
        _onDismiss.value = value
    }
    fun actualizarTitulo(it: String) {
        _tituloBottomSheet.value = it
    }
    fun IconoSelecionado(iconoSeleccionado: CategoryCrear) {
        _selectedCategoryGastos.value = iconoSeleccionado
    }



    fun crearNuevaCategoriaDeGastos(itemModel: UsuarioCreaCatGastoModel) {
        viewModelScope.launch {
            insertUserCatGastoUsecase(itemModel)
            isActivatedTrue()//activa el boton creado para borrar todoo
            limpiandoCampoBottomSheet()
        }
    }

    fun selectedParaEditar(itemModel: UsuarioCreaCatGastoModel, iconoSeleccionado: Int) {
        viewModelScope.launch {
            _idDelElemento.value = itemModel.id // para compartirlo con la otra funcion
            _tituloBottomSheet.value = itemModel.nombreCategoria
            _selectedCategoryGastos.value = CategoryCrear(name = "", iconoSeleccionado)
            _isEditarSeleccion.value = true //activa para actualizar al momento de guardar
            _onDismiss.value = true //abre el bottomSheet
        }
    }

    fun actualizandoItem(itemModel: UsuarioCreaCatGastoModel) {
        viewModelScope.launch {
            getUserCatGastoUsecase().map { lista ->
                val movimientoActualizado:UsuarioCreaCatGastoModel? = lista.map { itemViejo ->
                    if (itemViejo.id == _idDelElemento.value) {
                        //cambiandoles los valores a los items
                        itemViejo .nombreCategoria = itemModel.nombreCategoria
                        itemViejo .categoriaIcon = itemModel.categoriaIcon
                    }
                    itemViejo  // Devolver el movimiento actualizado o sin cambios
                }.find { it.id == _idDelElemento.value } // Encontrar el movimiento actualizado
                movimientoActualizado // Devolver el movimiento actualizado

            }.collect { movimientoActualizado ->
                if (movimientoActualizado != null) {
                    // Si se encontró y actualizó el movimiento, se pasa a la función de actualización individual
                    updateUserCatGastoUsecase(movimientoActualizado)
                    limpiandoCampoBottomSheet()
                } else {
                    // Manejar el caso en el que no se encuentre el elemento a actualizar
                    // Puede ser útil lanzar una excepción o manejarlo de alguna otra manera
                }
            }
        }
    }

    fun eliminarItemSelected(
        itemModel: UsuarioCreaCatGastoModel
    ) {
        viewModelScope.launch {
            deleteUserCatGastoUsecase(itemModel) //elimina el item seleccionado
            limpiandoElementoViejo(itemModel)//elimina de la lista predeterminada
            limpiandoCampoBottomSheet()
        }
    }
    fun borrandoLista() {
        viewModelScope.launch {
            val listaFlow= getUserCatGastoUsecase().first()
            // Elimina todas las categorias de la base de datos
            for (item in listaFlow) {
                deleteUserCatGastoUsecase(item)
                limpiandoElementoViejo(item)
            }
            limpiandoCampoBottomSheet()
        }
    }

    private fun limpiandoCampoBottomSheet() {
        _tituloBottomSheet.value = "" //limpia textField
        _selectedCategoryGastos.value = null //limpia seleccion
        _isEditarSeleccion.value = false //desactiva para no editar
    }
    private fun limpiandoElementoViejo(itemViejo: UsuarioCreaCatGastoModel){
        val categoriaEliminar = CategoryGasto(
            itemViejo.nombreCategoria,
            itemViejo.categoriaIcon.toInt()
        )
        //REMUEVE EL ITEM ELIMINADO DE LA LISTA DE CATEGORIA PREDETERMINADA
        categoriesGastos.removeAll { it == categoriaEliminar }
    }
}

