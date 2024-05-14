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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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


    private val _selectedCategoryIngresos = mutableStateOf<CategoryCrear?>(null)
    val selectedCategoryIngresos: State<CategoryCrear?> = _selectedCategoryIngresos

    fun isActivatedTrue() {
        _isActivated.value = true
    }

    fun isActivatedFalse() {
        _isActivated.value = false
    }

    fun crearNuevaCategoriaDeIngresos(titulo: String, icon: Int) {
        viewModelScope.launch {
            insertUserCatIngresoUsecase(
                UsuarioCreaCatIngresosModel(
                    nombreCategoria = titulo,
                    categoriaIcon = icon.toString()
                )
            )
            isActivatedTrue()
        }
    }


    fun editarItemSelected(nombreCategoria: String, categoriaIcon: String) {

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
}