package com.example.gastosdiariosjetapckcompose.features.home


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.GetUserCatIngresoUsecase
import com.example.gastosdiariosjetapckcompose.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryGasto
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryIngreso
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import com.example.gastosdiariosjetapckcompose.domain.model.ImagenSeleccionadaModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalIngresosModel
import com.example.gastosdiariosjetapckcompose.domain.model.UsuarioCreaCatGastoModel
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesGastos
import com.example.gastosdiariosjetapckcompose.domain.model.categoriesIngresos
import com.example.gastosdiariosjetapckcompose.domain.usecase.GastosPorNameCatCaseUse
import com.example.gastosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatGasto.GetUserCatGastoUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.GetFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.InsertFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.UpdateFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.imagenSeleccionada.AddUriImagenSeleccionadaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.imagenSeleccionada.GetUriImagenSeleccionadaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.imagenSeleccionada.UpdateUriImagenSeleccionadaUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.AddCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.GetCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.UpdateCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.AddMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.GetMovimientosUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.GetTotalIngresosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.InsertTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.InsertTotalIngresosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.UpdateTotalGastosUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.totalesRegistro.UpdateTotalIngresosUseCase
import com.example.gastosdiariosjetapckcompose.features.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val addCurrentMoneyUseCase: AddCurrentMoneyUseCase,
    private val getCurrentMoneyUseCase: GetCurrentMoneyUseCase,
    private val updateCurrentMoneyUseCase: UpdateCurrentMoneyUseCase,

    private val insertFechaUsecase: InsertFechaUsecase,
    private val getFechaUsecase: GetFechaUsecase,
    private val updateFechaUsecase: UpdateFechaUsecase,

    private val addMovimientosUseCase: AddMovimientosUsecase,
    private val getMovimientosUsecase: GetMovimientosUsecase,

    private val addUriImagenSeleccionada: AddUriImagenSeleccionadaUseCase,
    private val getUriImagenSeleccionadaUseCase: GetUriImagenSeleccionadaUseCase,
    private val updateUriImagenSeleccionadaUseCase: UpdateUriImagenSeleccionadaUseCase,

    private val insertTotalIngresosUseCase: InsertTotalIngresosUseCase,
    private val updateTotalIngresosUseCase: UpdateTotalIngresosUseCase,
    private val getTotalIngresosUseCase: GetTotalIngresosUseCase,

    private val insertTotalGastosUseCase: InsertTotalGastosUseCase,
    private val updateTotalGastosUseCase: UpdateTotalGastosUseCase,
    private val getTotalGastosUseCase: GetTotalGastosUseCase,
    //muestralas registro de categorias unicas
    private val gastosPorNameCatCaseUse: GastosPorNameCatCaseUse,
    //dataStore almacenamiento
    private val dataStorePreferences: DataStorePreferences,
    //obteniendo lista creada por el usuario
    private val getUserCatGastoUsecase: GetUserCatGastoUsecase,
    private val getUserCatIngresoUsecase: GetUserCatIngresoUsecase
) : ViewModel() {

    private val _selectedImageUri = MutableLiveData<String>()
    val selectedImageUri: LiveData<String> = _selectedImageUri

    private var _dineroActual = MutableLiveData<Double>()
    val dineroActual: LiveData<Double> = _dineroActual

    private var _mostrandoDineroTotalIngresos = MutableLiveData<Double>()
    val mostrandoDineroTotalIngresos: LiveData<Double> = _mostrandoDineroTotalIngresos

    private var _mostrandoDineroTotalGastos = MutableLiveData<Double>()
    val mostrandoDineroTotalGastos: LiveData<Double> = _mostrandoDineroTotalGastos


    //variable donde el usuario ingresa la cantidad de plata
    private val _cantidadIngresada = MutableLiveData<String>()
    val cantidadIngresada: LiveData<String> = _cantidadIngresada

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    //variable donde el usuario elige la categoria
    private val _categoryIcono = MutableLiveData<ImageVector>()
    val categoryIcono: LiveData<ImageVector> = _categoryIcono

    //variable donde el usuario escribe en la descripcion opcional
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _isChecked = mutableStateOf(false)
    val isChecked: State<Boolean> = _isChecked

    //variable para mostrar el dialogo de transaccion
    private val _showDialogTransaction = MutableLiveData<Boolean>()
    val showDialogTransaction: LiveData<Boolean> = _showDialogTransaction

    private val _showDialogFecha = mutableStateOf(false)
    val showDialogFecha: State<Boolean> = _showDialogFecha

    //variable para solo mostrar los dias restantes
    private val _diasRestantes = MutableLiveData<Int>(0)
    val diasRestantes: LiveData<Int> = _diasRestantes

    private val _fechaElegidaConGuion = MutableLiveData<String?>()

    private val _fechaElegidaBarra = MutableLiveData<String?>()
    val fechaElegidaBarra: LiveData<String?> = _fechaElegidaBarra

    private val _selectedOptionFechaMaxima = MutableStateFlow<Int>(0)
    val selectedOptionFechaMaxima: StateFlow<Int> = _selectedOptionFechaMaxima

    private val _limitePorDia = MutableLiveData(0.0)
    val limitePorDia: LiveData<Double> = _limitePorDia

    private val _botonActivado = MutableLiveData<Int>()
    val botonIngresosActivado:LiveData<Int> = _botonActivado

    private val _enabledBotonGastos = mutableStateOf<Boolean>(false)
    val enabledBotonGastos:State<Boolean> = _enabledBotonGastos

    init {
        viewModelScope.launch {
            val fechaActual = LocalDate.now()
            val dineroDisponible = getCurrentMoneyUseCase().money
            _dineroActual.value = dineroDisponible

            _fechaElegidaBarra.value = mostrarFechaConBarraGuardada()
            _fechaElegidaBarra.value?.let { mostrarDiasRestantes(it) }
            _limitePorDia.value = calcularLimitePorDia(dineroDisponible)

            val totalIngresos = getTotalIngresosUseCase()?.totalIngresos
            _mostrandoDineroTotalIngresos.value = totalIngresos ?: 0.0
            val totalGastos = getTotalGastosUseCase()?.totalGastos
            _mostrandoDineroTotalGastos.value = totalGastos ?: 0.0

            listCatGastosNueva()//actualizando la lista de categoria del menu desplegable
            listCatIngresosNueva()//actualizando la lista de categoria del menu desplegable

            //obteniendo fecha guardada maxima por el usuario
            dataStorePreferences.getFechaMaximoMes().collect { mesMaximo ->
                _selectedOptionFechaMaxima.value = mesMaximo.numeroGuardado.toInt()
                Log.d("fechaMaxima", "viewModelHome = ${_selectedOptionFechaMaxima.value}")
            }

            _mostrandoDineroTotalIngresos.value = getTotalIngresosUseCase()?.totalIngresos

            Log.d("limitePordia", "${_limitePorDia.value}")
            // Verificar si fechaGuardada es nula antes de intentar analizarla
            if (_fechaElegidaBarra.value != null) {
                Log.d("fechaElegida", "${_fechaElegidaBarra.value}")
                try {
                    //parseando fecha a LocalDate
                    val localDate = convertirFechaAGuion(_fechaElegidaBarra.value!!)
                    val fechaParseadaAGuion = LocalDate.parse(localDate)

                    // ej: 2023-12-12
                    //si la fecha actual es igual que la fecha guardada
                    if (fechaActual.isEqual(fechaParseadaAGuion)) {
                        //si aun tiene dinero el usuario al finalizar la fecha elegida
                        if (dineroDisponible != 0.0) {
                            Log.d("miApp", "actualizando valores")
                            actualizarFechaUnMesMas(fechaActual, fechaParseadaAGuion)
                            updateGastosTotal(TotalGastosModel(totalGastos = 0.0))
                            updateIngresoTotal(TotalIngresosModel(totalIngresos = dineroDisponible))
                            //actualizando el liveData con el nuevo valor maximo del progress
                            _mostrandoDineroTotalIngresos.value = dineroDisponible
                            Log.d("fechaIgualDiaSeleccion", "${_fechaElegidaBarra.value}")
                            initMostrandoAlUsuario(_fechaElegidaBarra.value!!, dineroDisponible)
                        } else {
                            //si el usuario no tiene dinero al llegar la fecha entonces se reinician los datos del progress
                            reseteandoProgress()
                        }
                    } else {
                        //si aun no es la fecha elegida por el usuario se mostrara esto
                        Log.d("miApp", "Ingresando a los datos")
                        initMostrandoAlUsuario(_fechaElegidaBarra.value!!, dineroDisponible)
                    }
                } catch (ex: DateTimeParseException) {
                    manejarErrorDeFecha()
                }
            } else {
                // Si fechaGuardada es nula y dias restantes tambien se asigna un valor predeterminado
                manejarFechayDiasRestantesNulos()
            }
        }
    }

    private fun reseteandoProgress() {
        viewModelScope.launch {
            updateIngresoTotal(TotalIngresosModel(totalIngresos = 0.0))
            updateGastosTotal(TotalGastosModel(totalGastos = 0.0))
        }
    }

    private fun mostrarDineroActual() {
        // Lógica relacionada con el dinero
        viewModelScope.launch {
            _dineroActual.value = getCurrentMoneyUseCase().money
        }
    }

    fun mostrandoDineroTotalProgress() {
        //totalDinero borrado
        viewModelScope.launch {
            _mostrandoDineroTotalIngresos.value = getTotalIngresosUseCase()?.totalIngresos
            Log.d("totalDinero", "maximo = ${_mostrandoDineroTotalIngresos.value}")
        }
    }

    // Método para obtener y actualizar la fecha guardada
    suspend fun mostrarFechaConBarraGuardada(): String? {
        return viewModelScope.async {
            val fechaConBarra = getFechaUsecase()?.fechaSave
            Log.d("initFechaGuardada", "$fechaConBarra")
            fechaConBarra
        }.await()
    }

    fun mostrarFechaBorradaAlUSuario() {
        //fecha con barra mostrada al usuario borrada
        viewModelScope.launch {
            val item = getFechaUsecase()?.fechaSave
            if (item == null) {
                _fechaElegidaBarra.value = ""
            }
        }
    }

    fun mostrarDiasrestantesBorradaAlUSuario() {
        //dias restantes borrado
        viewModelScope.launch {
            val item = getFechaUsecase()?.fechaSave?.toInt()
            if (item == null) {
                _diasRestantes.value = 0
            }
        }
    }

    fun mostrandoDineroTotalProgressGastos() {
        //totalDinero gastado borrado
        viewModelScope.launch {
            _mostrandoDineroTotalGastos.value = getTotalGastosUseCase()?.totalGastos
        }
    }

    private fun actualizarFechaUnMesMas(fechaActual: LocalDate, fechaParseada: LocalDate) {
        viewModelScope.launch {
            //se agrega un mes a la fecha guardada
            var nuevoMes = agregandoUnMes(fechaActual, fechaParseada)
            nuevoMes = convertidorFechaABarra(nuevoMes)
            //se actualiza en la base de datos la nueva fecha
            actualizandoFecha(FechaSaveModel(fechaSave = nuevoMes, isSelected = false))
        }
    }

    private fun initMostrandoAlUsuario(getFechaConBarra: String, dineroActual: Double) {
        mostrarImagenPerfil()
        mostrarDineroActual()
        _limitePorDia.value = calcularLimitePorDia(dineroActual)
        mostrarFecha(getFechaConBarra)
        mostrarDiasRestantes(getFechaConBarra)
        mostrandoDineroTotalProgress()
        mostrandoDineroTotalProgressGastos()
        mostrarLimitePorDiaAlUsuario()
    }

    fun mostrarDineroTotal() {
        //dineroActual borrado
        viewModelScope.launch {
            _dineroActual.value = getCurrentMoneyUseCase().money
        }
    }


    private fun mostrandoAlUsuario(fechaConBarra: String) {
        mostrarFecha(fechaConBarra)
        //_diasRestantes.value = diasRestantes(fechaSave)
        mostrarDiasRestantes(fechaConBarra)
        //_limitePorDia.value = calcularLimitePorDia()
        _limitePorDia.value = _dineroActual.value?.let { calcularLimitePorDia(it) }
    }

    private fun mostrarFecha(fechaConBarra: String) {
        _fechaElegidaBarra.value = fechaConBarra
        Log.d("mostrarFecha", "${_fechaElegidaBarra.value}")
    }

    private fun mostrarDiasRestantes(fechaConBarra: String) {
        val fechaConGuion = convertirFechaAGuion(fechaConBarra)
        Log.d("fechaConGuion", "$fechaConGuion")
        //se manejan con dias con guion por eso hay que parsearlo antes de enviar
        _diasRestantes.value = diasRestantes(fechaConGuion)//retorna fechaConGuion
        Log.d("diasRestantes", "${_diasRestantes.value}")
    }

    private fun manejarErrorDeFecha() {
        // Manejar el caso en el que la fecha seleccionada no se pudo parsear correctamente
        Log.d("MiApp", "Error al parsear la fecha")
    }

    private fun convertidorFechaABarra(fechaGuion: String): String {
        // fechaGuion sería "2024-04-30"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(fechaGuion, formatter)
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    private fun manejarFechayDiasRestantesNulos() {
        // Si fechaGuardada es nula, maneja ese caso aquí
        _fechaElegidaBarra.value = "Selecciona Fecha"
        _diasRestantes.value = 0
        Log.d("MiApp", "fechaElegidaBarra es nulo")
    }

    private fun insertUpdateFecha(fechaConBarra: String) {
        viewModelScope.launch {
            Log.d("miApp", "nuevaFecha = ${fechaConBarra}")
            try {
                if (getFechaUsecase() == null) {
                    //si es true se ingresa por primera vez
                    insertandoFecha(FechaSaveModel(fechaSave = fechaConBarra, isSelected = false))
                    Log.d("insertFecha", "insertandoFecha = $fechaConBarra")
                } else {
                    //si es false se actualiza en la base de datos y por las dudas se sigue manteniendo que es false
                    actualizandoFecha(FechaSaveModel(fechaSave = fechaConBarra, isSelected = false))
                    Log.d("actualizandoFecha", "actualizandoFecha = $fechaConBarra")
                }
            } catch (e: DateTimeParseException) {
                // Manejar la excepción en caso de que ocurra un error al analizar la fecha
                Log.e("miApp", "Error al analizar la fecha: $fechaConBarra", e)
                // Aquí puedes realizar alguna acción adicional, como mostrar un mensaje de error al usuario
            }
        }
    }

    private fun insertandoFecha(item: FechaSaveModel) {
        viewModelScope.launch {
            insertFechaUsecase(item)//insertando en la base de datos
            _fechaElegidaBarra.value = item.fechaSave
            mostrandoAlUsuario(_fechaElegidaBarra.value.toString())
            mostrarFecha(_fechaElegidaBarra.value.toString())
            //_diasRestantes.value = diasRestantes(fechaSave)
            mostrarDiasRestantes(_fechaElegidaBarra.value.toString())
            _limitePorDia.value = _dineroActual.value?.let { calcularLimitePorDia(it) }
        }
    }

    private fun actualizandoFecha(item: FechaSaveModel) {
        //obteniendo fecha con guion ej 2024-04-02
        viewModelScope.launch {
            val nuevaFecha = getFechaUsecase()
            if (nuevaFecha != null) {
                nuevaFecha.fechaSave = item.fechaSave
                nuevaFecha.isSelected = false
                Log.d("miApp", "Actualizando fecha total = $nuevaFecha")
                updateFechaUsecase(nuevaFecha)
                mostrandoAlUsuario(nuevaFecha.fechaSave)
            } else {
                Log.e("miApp", "El objeto nuevaFecha es nulo")
                // Manejar el caso en el que getFechaUsecase() devuelve null, si es necesario
            }
        }
    }

    fun enviandoFechaElegida(date: String) {
        //date tiene valor: dia/mes/año ej 10/04/2024
        _fechaElegidaBarra.value = date //muestra 01/04/2024
        val fechaConGuion = convertirFechaAGuion(date)//devuelve 2024-04-01
        _fechaElegidaConGuion.value = fechaConGuion
        insertUpdateFecha(fechaConBarra = date)
        Log.d("enviandoFechaElegida", fechaConGuion)
    }

    fun convertirFechaAGuion(date: String): String {
        // date sería "30/04/2024"
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val localDate = LocalDate.parse(date, formatter)
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }


    private fun diasRestantes(getFechaGuion: String?): Int {
        if (getFechaGuion.isNullOrEmpty()) {
            return 0
        }
        val fechaActual: LocalDate = LocalDate.now()
        val fLocalDate = LocalDate.parse(getFechaGuion)
        var dia: Long = ChronoUnit.DAYS.between(fechaActual, fLocalDate)
        dia += 1
        return dia.toInt()
        Log.d("diasRestantes", "diasrestantes: $dia")

    }

    fun calculadora(nuevoValor: String) {
        viewModelScope.launch {
            // Obtener el valor de isChecked de manera síncrona
            val getChecked = getCurrentMoneyUseCase().isChecked
            Log.d("miApp", "getChecked: $getChecked")

            // Calcula el nuevo dinero según la opción elegida por el usuario
            val nuevoTotalAgregado = when (_isChecked.value) {
                true -> {
                    sharedLogic.agregarDineroAlTotal(
                        nuevoValor.toDouble(),
                        _mostrandoDineroTotalIngresos.value ?: 0.0
                    )
                }

                else -> {
                    //cuando el usuario elige gastos se agrega al valor existente, sumandolo
                    sharedLogic.agregarDineroAlTotal(
                        nuevoValor.toDouble(),
                        _mostrandoDineroTotalGastos.value ?: 0.0
                    )
                }
            }
            Log.d("miApp", "nuevoTotalAgregado en gastos es: $nuevoTotalAgregado")

            val nuevoDinero = when (_isChecked.value) {
                true -> {
                    agregarDinero(nuevoValor.toDouble(), _dineroActual.value ?: 0.0)
                }

                else -> {
                    maxOf(restarDinero(nuevoValor), 0.0)
                }
            }

            // Actualiza el valor en _dineroActual.value y en la base de datos
            _dineroActual.value = nuevoDinero

            if (_isChecked.value == true) {
                if (getChecked) {
                    // Si la opción seleccionada es ingresos
                    insertBaseDatos(CurrentMoneyModel(money = nuevoDinero))
                    insertandoPrimerIngresoProgress(TotalIngresosModel(totalIngresos = nuevoTotalAgregado))
                    insertandoPrimerGastoProgress(TotalGastosModel(totalGastos = 0.0))
                } else {
                    // Si ya hay datos en la base de datos para ingresos, actualizar el total de ingresos
                    updateBaseDatos(CurrentMoneyModel(money = nuevoDinero))
                    updateIngresoTotal(TotalIngresosModel(totalIngresos = nuevoTotalAgregado))
                }
            } else {
                Log.d(
                    "miApp",
                    "nuevoTotalAgregado antes de actualizar el total gasto: $nuevoTotalAgregado"
                )
                if (nuevoDinero == 0.0 && nuevoTotalAgregado > 0.0) {
                    //reseteando el progress
                    reseteandoProgress()
                } else {
                    // Si el usuario eligió gastos, actualizar la base de datos con el nuevo dinero
                    updateBaseDatos(CurrentMoneyModel(money = nuevoDinero))
                    updateGastosTotal(TotalGastosModel(totalGastos = nuevoTotalAgregado))

                }
            }
            _limitePorDia.value = calcularLimitePorDia(nuevoDinero)
        }
    }

    private fun calcularLimitePorDia(dineroActual: Double): Double {
        val limitePorDia =
            if (_diasRestantes.value != null && dineroActual != null && _diasRestantes.value != 0) {
                dineroActual / _diasRestantes.value!!.toDouble()
            } else {
                0.0
            }
        Log.d("funLimitePorDia", "fun limite: ${_limitePorDia.value}")
        Log.d("funLimitePorDia", "fun dineroActual: $dineroActual")
        return limitePorDia
    }

    fun mostrarLimitePorDiaAlUsuario() {
        //limite por dia borrado
        viewModelScope.launch {
            _limitePorDia.value = getCurrentMoneyUseCase().money
        }
    }


//    private fun calcularLimitePorDia(): Double {
//        viewModelScope.launch {
//           val dineroActual =  getCurrentMoneyUseCase().money
//            val limitePorDia =
//                if (_diasRestantes.value != null && dineroActual != null && _diasRestantes.value != 0) {
//                    dineroActual / _diasRestantes.value!!.toDouble()
//                } else {
//                    0.0
//                }
//            //muestra al usuario directamente
//            _limitePorDia.value = limitePorDia
//            Log.d("funLimitePorDia", "calcularLimitePorDia: ${_limitePorDia.value}")
//            Log.d("funLimitePorDia", "dineroActual: $dineroActual")
//        }
//        return limitePorDia
//    }

    private fun insertBaseDatos(item: CurrentMoneyModel) {
        // Agregar el valor a la base de datos
        viewModelScope.launch {
            val newValor = CurrentMoneyModel(money = item.money, isChecked = false)
            Log.d("miApp", "INSERTANDO : $newValor")
            addCurrentMoneyUseCase(newValor)
            _dineroActual.value = getCurrentMoneyUseCase().money

        }
    }

    private fun insertandoPrimerIngresoProgress(item: TotalIngresosModel) {
        viewModelScope.launch {
            val primerIngreso = TotalIngresosModel(totalIngresos = item.totalIngresos)
            insertTotalIngresosUseCase(primerIngreso)
            _mostrandoDineroTotalIngresos.value = getTotalIngresosUseCase()?.totalIngresos
            Log.d("miApp", "primerIngresoTotal ${_mostrandoDineroTotalIngresos.value}")
        }
    }

    private fun insertandoPrimerGastoProgress(item: TotalGastosModel) {
        viewModelScope.launch {
            val primerGasto = TotalGastosModel(totalGastos = item.totalGastos)
            insertTotalGastosUseCase(primerGasto)
            _mostrandoDineroTotalGastos.value = getTotalGastosUseCase()?.totalGastos
            Log.d("miApp", "primerGastoTotal ${_mostrandoDineroTotalGastos.value}")
        }
    }

    private fun updateBaseDatos(item: CurrentMoneyModel) {
        viewModelScope.launch {
            val newValor = getCurrentMoneyUseCase()
            newValor.money = item.money
            newValor.isChecked = false
            updateCurrentMoneyUseCase(newValor)
            _dineroActual.value = getCurrentMoneyUseCase().money
            Log.d("miApp", "ACTUALIZANDO : ${_dineroActual.value}")
        }
    }

    private fun updateIngresoTotal(item: TotalIngresosModel) {
        viewModelScope.launch {
            val totalIngresosActual = getTotalIngresosUseCase()
            // Sumar solo el nuevo ingreso al total actual
            totalIngresosActual?.totalIngresos = item.totalIngresos
            // Actualizar el total de ingresos en el repositorio
            if (totalIngresosActual != null) {
                updateTotalIngresosUseCase(totalIngresosActual)
            }
            _mostrandoDineroTotalIngresos.value = getTotalIngresosUseCase()?.totalIngresos
            Log.d("miApp", "actualizandoTotalIngresos: ${_mostrandoDineroTotalIngresos.value}")
        }
    }

    private fun updateGastosTotal(item: TotalGastosModel) {
        viewModelScope.launch {
            val totalGastosActual = getTotalGastosUseCase()
            totalGastosActual?.totalGastos = item.totalGastos
            // Actualizar el total de gastos en el repositorio con el nuevo valor
            if (totalGastosActual != null) {
                updateTotalGastosUseCase(totalGastosActual)
            }
            // Actualizar el LiveData para reflejar los cambios
            _mostrandoDineroTotalGastos.value = getTotalGastosUseCase()?.totalGastos
            Log.d("miApp", "actualizandoTotalGastos: ${_mostrandoDineroTotalGastos.value}")
        }
    }

    private fun agregarDinero(nuevoValor: Double, valorExistente: Double): Double {
        val resultado = BigDecimal(valorExistente + nuevoValor)
            .setScale(2, RoundingMode.HALF_EVEN)
        return resultado.toDouble()
    }

    private fun restarDinero(nuevoValor: String): Double {
        val dineroActual = _dineroActual.value ?: 0.0
        val resultado =
            BigDecimal(dineroActual - nuevoValor.toDouble()).setScale(
                2,
                RoundingMode.HALF_EVEN
            )
        return resultado.toDouble()
    }

    private fun agregandoUnMes(fechaActual: LocalDate, fechaParseada: LocalDate): String {
        var diff = ChronoUnit.MONTHS.between(fechaParseada, fechaActual)
        diff += 1
        val mes = fechaParseada.plus(diff, ChronoUnit.MONTHS)
        return mes.toString()
    }

    private fun listCatGastosNueva() {
        viewModelScope.launch {
            getUserCatGastoUsecase().collect { item ->
                item.map { element ->
                    val newCategory = CategoryGasto(
                        name = element.nombreCategoria,
                        icon = element.categoriaIcon.toInt()
                    )
                    //verifica si la categoria a agregar no esta repetida, si lo esta entonces no se agrega
                    //de esta manera nos aseguramos de que solo haya una
                    if (!categoriesGastos.contains(newCategory)) {
                        categoriesGastos.add(newCategory)
                    }
                }
            }
        }
    }

    private fun listCatIngresosNueva() {
        viewModelScope.launch {
            getUserCatIngresoUsecase().collect { item ->
                item.map { element ->
                    val newCategory = CategoryIngreso(
                        name = element.nombreCategoria,
                        icon = element.categoriaIcon.toInt()
                    )
                    //verifica si la categoria a agregar no esta repetida, si lo esta entonces no se agrega
                    //de esta manera nos aseguramos de que solo haya una
                    if (!categoriesIngresos.contains(newCategory)) {
                        categoriesIngresos.add(newCategory)
                    }
                }
            }
        }
    }

    //metodo que devuelve un true para que abra el dialogo de transaccion
    fun onShowDialogClickTransaction() {
        _showDialogTransaction.value = true
        if(_dineroActual.value == 0.0){
            _botonActivado.value = 1 // el boton ingreso esta activado
            _isChecked.value = true //elmenu de ingresos esta activado
            _enabledBotonGastos.value = false //elboton de gastos esta desactivado
        }else{
            _botonActivado.value = 0
            _isChecked.value = false
            _enabledBotonGastos.value = true
        }
    }

    fun onDialogClose() {
        _showDialogTransaction.value = false
        _cantidadIngresada.value = ""
        _description.value = ""
    }

    //obtiene el nuevo valor de la plata que agregamos
    private fun setCantidadIngresada(newValor: String) {
        _cantidadIngresada.value = newValor
    }

    fun setDescription(nuevaDescripcion: String) {
        _description.value = nuevaDescripcion
    }

    fun setIsChecked(isChecked: Boolean) {
        _isChecked.value = isChecked
    }

    fun crearTransaccion(
        cantidad: String,
        categoryName: String,
        description: String,
        categoryIcon: Int,
        isChecked: Boolean
    ) {

        viewModelScope.launch {
            val transaccion = MovimientosModel(
                iconResourceName = categoryIcon.toString(),
                title = categoryName,
                subTitle = description,
                cash = cantidad,
                select = isChecked
            )
            addMovimientosUseCase(transaccion)
        }
        onDialogClose() // Asegúrate de tener una función llamada onDialogClose en tu viewModel
        setCantidadIngresada("") // Limpia los campos después de agregar la transacción
        setDescription("") // Limpia los campos después de agregar la transacción
    }


    fun crearNuevaCategoriaDeGastos(nameCategory: String, icon: Int, cantidadIngresada: String) {
        // Esta función se llamará desde tu Composable para crear
        // una nueva categoría de gastos unicas en el registro
        viewModelScope.launch {
            gastosPorNameCatCaseUse(nameCategory, icon.toString(), cantidadIngresada)
        }
    }

    fun guardarRutaImagen(uri: ImagenSeleccionadaModel) {
        viewModelScope.launch {
            val selec = getUriImagenSeleccionadaUseCase()
            Log.d("miApp", "selec = ${selec.isChecked}")
            if (selec.isChecked) {
                val insertandoImagen = ImagenSeleccionadaModel(
                    uri = uri.uri,
                    isChecked = false
                )
                addUriImagenSeleccionada(insertandoImagen)
                Log.d(
                    "miApp",
                    "insertando imagen: id = ${insertandoImagen.id}," +
                            " uri = ${insertandoImagen.uri}," +
                            " isChecked = ${insertandoImagen.isChecked}"
                )
            } else {
                val nuevaImagen = getUriImagenSeleccionadaUseCase()
                nuevaImagen.uri = uri.uri
                nuevaImagen.isChecked = false
                updateUriImagenSeleccionadaUseCase(nuevaImagen)

                Log.d("miApp", "actualizando imagen: $nuevaImagen")
            }
            // Después de guardar o actualizar la imagen, se actualiza la interfaz de usuario
            mostrarImagenPerfil()
        }
    }

    private fun mostrarImagenPerfil() {
        viewModelScope.launch {
            _selectedImageUri.value = getUriImagenSeleccionadaUseCase().uri
            val prueba = getUriImagenSeleccionadaUseCase()
            Log.d("miApp", "mostrandoImagen = ${prueba.id}, ${prueba.uri}, ${prueba.isChecked}")
        }
    }
}