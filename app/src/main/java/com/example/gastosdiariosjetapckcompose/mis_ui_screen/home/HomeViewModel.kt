package com.example.gastosdiariosjetapckcompose.mis_ui_screen.home


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Ingresosdiariosjetapckcompose.domain.usecase.UsuarioCreaCatIngreso.GetUserCatIngresoUsecase
import com.example.gastosdiariosjetapckcompose.data.core.GlobalVariables.sharedLogic
import com.example.gastosdiariosjetapckcompose.R
import com.example.gastosdiariosjetapckcompose.data_base_manager.DataBaseManager
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryGasto
import com.example.gastosdiariosjetapckcompose.domain.model.CategoryIngreso
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import com.example.gastosdiariosjetapckcompose.domain.model.ImagenSeleccionadaModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalGastosModel
import com.example.gastosdiariosjetapckcompose.domain.model.TotalIngresosModel
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
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
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
    private val getUserCatIngresoUsecase: GetUserCatIngresoUsecase,
    private val dataBaseManager: DataBaseManager
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

    private val _showNuevoMes = mutableStateOf(false)
    val showNuevoMes: State<Boolean> = _showNuevoMes

    private val _fechaElegidaConGuion = MutableLiveData<String?>()

    private val _fechaElegidaBarra = MutableStateFlow("")
    val fechaElegidaBarra: StateFlow<String?> = _fechaElegidaBarra


    private val _selectedOptionFechaMaxima = MutableStateFlow<Int>(0)
    val selectedOptionFechaMaxima: StateFlow<Int> = _selectedOptionFechaMaxima

    private val _botonActivado = MutableLiveData<Int>()
    val botonIngresosActivado: LiveData<Int> = _botonActivado

    private val _enabledBotonGastos = mutableStateOf<Boolean>(false)
    val enabledBotonGastos: State<Boolean> = _enabledBotonGastos

    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState

    init {
        viewModelScope.launch {
            //obteniendo fecha guardada maxima por el usuario
            dataStorePreferences.getFechaMaximoMes().collect { it ->
                _selectedOptionFechaMaxima.value = it.numeroGuardado.toInt()
            }
        }
        val fechaActual = LocalDate.now()
        listCatGastosNueva()//actualizando la lista de categoria del menu desplegable
        listCatIngresosNueva()//actualizando la lista de categoria del menu desplegable
        calculandoInit(fechaActual)
    }
    fun onEventHandler(e:HomeEventHandler){
        when(e){
            is HomeEventHandler.GetCurrentMoney -> TODO()
            is HomeEventHandler.SetCurrentMoney -> sendDateElegida(e.money)
        }
    }


    private fun calculandoInit(fechaActual: LocalDate) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val dineroDisponible = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                val fechaConBarra = withContext(Dispatchers.IO) {
                    getFechaUsecase()?.fechaSave
                }
                _dineroActual.value = dineroDisponible

                // Verificar si fechaGuardada es nula antes de intentar analizarla
                if (fechaConBarra != null) {
                    //  parseando fecha a LocalDate
                    val localDate = convertirFechaAGuion(fechaConBarra)
                    val fechaParseadaAGuion = LocalDate.parse(localDate)

                    // ej: 2023-12-12
                    //si la fecha actual es igual que la fecha guardada
                    if (fechaActual.isEqual(fechaParseadaAGuion) || (fechaActual > fechaParseadaAGuion)) {
                        //si aun tiene dinero el usuario al finalizar la fecha elegida
                        if (dineroDisponible != 0.0) {
                            updateFechaUnMesMas(fechaActual, fechaParseadaAGuion)
                            updateGastosTotal(TotalGastosModel(totalGastos = 0.0))
                            updateIngresoTotal(TotalIngresosModel(totalIngresos = dineroDisponible))
                            dataBaseManager.deleteAllExpenses()
                            crearTransaccion(
                                cantidad = dineroDisponible.toString(),
                                categoryName = "Saldo restante",
                                description = "",
                                categoryIcon = R.drawable.ic_sueldo,
                                isChecked = true
                            )

                            //actualizando el liveData con el nuevo valor maximo del progress
                            _mostrandoDineroTotalIngresos.value = dineroDisponible

                            initMostrandoAlUsuario()
                        } else {
                            //si el usuario no tiene dinero al llegar la fecha
                            // entonces se reinician los datos del progress

                            reseteandoProgress()
                        }
                    } else {
                        //si aun no es la fecha elegida por el usuario se mostrara esto
                        initMostrandoAlUsuario()
                    }
                } else {
                    // Si fechaGuardada es nula y dias restantes tambien se asigna un valor predeterminado
                    manejarFechayDiasRestantesNulos()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                manejarFechayDiasRestantesNulos()
            }
        }
    }

    private fun reseteandoProgress() {
        //si el usuario gasto el dinero antes del dia de la fecha elegida
        viewModelScope.launch {
            dataBaseManager.deleteAllExpenses()
            dataBaseManager.deleteAllGastosPorCategory()
            updateCurrentMoney(CurrentMoneyModel(money = 0.0, isChecked = false))
            updateIngresoTotal(TotalIngresosModel(totalIngresos = 0.0))
            updateGastosTotal(TotalGastosModel(totalGastos = 0.0))
        }
    }

    private fun mostrandoDineroTotalProgress() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val dato = withContext(Dispatchers.IO) {
                    getTotalIngresosUseCase()?.totalIngresos
                }
                _mostrandoDineroTotalIngresos.postValue(dato ?: 0.0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun mostrandoDineroTotalProgressGastos() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) {
                    getTotalGastosUseCase()?.totalGastos
                }
                _mostrandoDineroTotalGastos.postValue(data ?: 0.0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateFechaUnMesMas(fechaActual: LocalDate, fechaParseada: LocalDate) {
        viewModelScope.launch {
            //se agrega un mes a la fecha guardada
            val nuevoMes: String = agregandoUnMes(fechaActual, fechaParseada)
            val fechaConBarra = convertidorFechaABarra(nuevoMes)
            _showNuevoMes.value = true
            //se actualiza en la base de datos la nueva fecha
            updateFecha(FechaSaveModel(fechaSave = fechaConBarra, isSelected = false))

            // al agregar un nuevo mes, eliminamos tambien la lista por categorias para que no se guarde nada en el grafico
            // Verificar si es el último día del mes actual
            val ultimoDiaDelMes = fechaActual.lengthOfMonth()
            val esUltimoDiaDelMes = fechaActual.dayOfMonth == ultimoDiaDelMes
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(nuevoMes, formatter)
            // Eliminar la lista solo si no estamos en el último día del mes actual
            if (localDate > fechaActual) {
                dataBaseManager.deleteAllGastosPorCategory()
            }
        }
    }

    fun initMostrandoAlUsuario() {
        mostrarDineroActual()
        mostrarImagenPerfil()
        mostrandoDineroTotalProgress()
        mostrandoDineroTotalProgressGastos()
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val datoDinero = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                val datoFecha = withContext(Dispatchers.IO) {
                    getFechaUsecase()?.fechaSave
                }

                if (datoFecha != null) {
                    mostrandoAlUsuario(datoFecha)//muestra fecha y dias restantes
                    sharedLogic._limitePorDia.value = sharedLogic.calcularLimitePorDia(datoDinero)
                } else {
                    // Si datoFecha es null, no se realiza ninguna acción adicional
                    sharedLogic._diasRestantes.value = 0
                    sharedLogic._limitePorDia.value = 0.0
                    _fechaElegidaBarra.value = ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("mostrarDineroTotal", "Error al obtener el dinero total: ${e.message}")
            }
        }
    }

    private fun mostrarDineroActual() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) {
                    getCurrentMoneyUseCase().money
                }
                _dineroActual.value = data
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("mostrarDineroTotal", "Error al obtener el dinero total: ${e.message}")
            }

        }
    }

    private fun mostrandoAlUsuario(fechaConBarra: String) {
        mostrarFecha(fechaConBarra)
        mostrarDiasRestantes(fechaConBarra)
        sharedLogic._limitePorDia.value =
            _dineroActual.value?.let { sharedLogic.calcularLimitePorDia(it) } ?: 0.0
    }

    private fun mostrarFecha(fechaConBarra: String) {
        //fechaConBarra tiene el formato 05/07/2024
        _fechaElegidaBarra.value = convertirFechaPersonalizada(fechaConBarra)
        //muestra 05 Jul 2024
    }

    private fun mostrarDiasRestantes(fechaConBarra: String) {
        Log.d("diasRestantesAntes", fechaConBarra)
        val fechaConGuion = convertirFechaAGuion(fechaConBarra)
        //se manejan con dias con guion por eso hay que parsearlo antes de enviar
        sharedLogic._diasRestantes.value = diasRestantes(fechaConGuion)
    }

    private fun convertidorFechaABarra(fechaGuion: String): String {
        // fechaGuion sería "2024-04-30"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(fechaGuion, formatter)
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    private fun manejarFechayDiasRestantesNulos() {
        // Si fechaGuardada es nula, maneja ese caso aquí
        _fechaElegidaBarra.value = ""
        sharedLogic._diasRestantes.value = 0
    }

    private fun insertUpdateFecha(fechaConBarra: String) {
        viewModelScope.launch {
            try {
                if (getFechaUsecase() == null) {
                    //si es true se ingresa por primera vez
                    insertFecha(
                        FechaSaveModel(fechaSave = fechaConBarra, isSelected = false),
                        fechaConBarra
                    )
                } else {
                    //si es false se actualiza en la base de datos y por las dudas se sigue manteniendo que es false
                    updateFecha(FechaSaveModel(fechaSave = fechaConBarra, isSelected = false))
                }
            } catch (e: DateTimeParseException) {
                Log.e("miApp", "Error al analizar la fecha: $fechaConBarra", e)
            }
        }
    }

    private fun insertFecha(item: FechaSaveModel, fechaConBarra: String) {
        viewModelScope.launch {
            insertFechaUsecase(item)//insertando en la base de datos
            //  _fechaElegidaBarra.value = item.fechaSave
            mostrandoAlUsuario(fechaConBarra)

            // mostrarDiasRestantes(_fechaElegidaBarra.value.toString())
            sharedLogic._limitePorDia.value =
                _dineroActual.value?.let { sharedLogic.calcularLimitePorDia(it) } ?: 0.0
        }
    }

    private fun updateFecha(item: FechaSaveModel) {
        //obteniendo fecha con guion ej 2024-04-02
        viewModelScope.launch {
            val newDate = getFechaUsecase()
            if (newDate != null) {
                newDate.fechaSave = item.fechaSave
                newDate.isSelected = false
                updateFechaUsecase(newDate)
                mostrandoAlUsuario(newDate.fechaSave)
            } else {
                Log.e("miApp", "El objeto nuevaFecha es nulo")
            }
        }
    }

    fun sendDateElegida(date: String) {
        // date me pasa ej 17/7/2024 pasarla a 17/07/2024
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            val dateFormat: Date? = sdf.parse(date)
            val formattedDate: String = sdf.format(dateFormat!!)
            insertUpdateFecha(fechaConBarra = formattedDate)
        } catch (e: ParseException) {
            Log.e("Error", "Error al formatear la fecha: ${e.message}")
        }
    }

    private fun convertirFechaPersonalizada(date: String): String {
        val partesFecha = date.split("/") // Dividir la fecha en partes: [dia, mes, año]
        if (partesFecha.size == 3) {
            val dia = partesFecha[0]
            val mes = getNameMes(partesFecha[1].toInt()) // Obtener el nombre del mes
            val anio = partesFecha[2]
            return "$dia $mes.$anio" // Formato personalizado: 27 jun.2024
        } else {
            Log.e("convertirFechaPersonalizada", "Fecha en formato incorrecto: $date")
            return "Formato incorrecto"
        }
    }

    private fun getNameMes(mes: Int): String {
        return DateFormatSymbols().months[mes - 1].substring(0, 3).lowercase(Locale.ROOT)
            .replaceFirstChar {
                if (it.isLowerCase())
                    it.titlecase(Locale.ROOT)
                else it.toString()
            } // Obtener el nombre del mes en formato corto y capitalizado
    }

    private fun convertirFechaAGuion(date: String): String {
        // Verificar si la fecha ya está en el formato con guion
        try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(date, formatter)
            Log.d("convertirFechaAGuion", "Fecha ya está en formato con guion: $date")
            return date
        } catch (e: DateTimeParseException) {
            // Si no está en el formato yyyy-MM-dd, intentar convertir desde dd/MM/yyyy
            try {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val localDate = LocalDate.parse(date, formatter)
                val fechaConGuion = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                Log.d("convertirFechaAGuion", "Fecha con guion: $fechaConGuion")
                return fechaConGuion
            } catch (e: DateTimeParseException) {
                Log.e("convertirFechaAGuion", "Error al convertir la fecha: $date", e)
                return ""
            }
        }
    }

    private fun diasRestantes(getFechaGuion: String?): Int {
        if (getFechaGuion.isNullOrEmpty()) {
            return 0
        }
        val fechaActual: LocalDate = LocalDate.now()
        val fLocalDate = LocalDate.parse(getFechaGuion)
        val dia: Long = ChronoUnit.DAYS.between(fechaActual, fLocalDate)
        return dia.toInt()
    }

    fun cantidadIngresada(cantidadIngresada: String, userSelected: Boolean) {
        _cantidadIngresada.value = cantidadIngresada
        viewModelScope.launch {
            val data = getCurrentMoneyUseCase()
            val dataIngresos =
                withContext(Dispatchers.IO) { getTotalIngresosUseCase()?.totalIngresos ?: 0.0 }
            val dataGastos =
                withContext(Dispatchers.IO) { getTotalGastosUseCase()?.totalGastos ?: 0.0 }

            calculadoraDialog(
                cantidadIngresada.toDouble(),
                data,
                dataIngresos,
                dataGastos,
                userSelected
            )
        }
    }

    private fun calculadoraDialog(
        cantidadIngresada: Double,
        data: CurrentMoneyModel,
        dataIngresos: Double,
        dataGastos: Double,
        userSelected: Boolean
    ) {
        viewModelScope.launch {
            // Calcula el nuevo dinero según la opción elegida por el usuario
            val nuevoTotal = when (userSelected) {
                true -> {
                    addDiner(cantidadIngresada, dataIngresos)
                }

                else -> {
                    addDiner(cantidadIngresada, dataGastos)
                }
            }
            val dineroActual = when (userSelected) {
                true -> {
                    addDiner(cantidadIngresada, data.money ?: 0.0)
                }

                else -> {
                    maxOf(restarDinero(cantidadIngresada.toString()), 0.0)
                }
            }

            //si el usuario eligio ingresos
            when (userSelected) {
                true -> if (data.isChecked) {
                    //data.isChecked es true entonces significa que no hay nadad aun guardado
                    insertBaseDatos(CurrentMoneyModel(money = dineroActual))
                    insertPrimerIngresoProgress(TotalIngresosModel(totalIngresos = nuevoTotal))
                    insertPrimerGastoProgress(TotalGastosModel(totalGastos = 0.0))
                } else {
                    //Si el usuario eligio Ingresos pero en data.isChecked es false
                    // significa que ya hay datos guardatos, por ende se actualizaran los ingresos
                    // Si ya hay datos en Room se actualiza el totalIngresos
                    updateIngresoTotal(TotalIngresosModel(totalIngresos = nuevoTotal))
                }
                //Si el usuario eligio Gastos
                false -> {
                    //si el usuario eligio gastos
                    if (dineroActual == 0.0 && nuevoTotal > 0.0) {
                        //reseteando el progress
                        reseteandoProgress()
                    } else {
                        // Si el usuario eligió gastos, actualizar la base de datos con el nuevo dinero
                        updateGastosTotal(TotalGastosModel(totalGastos = nuevoTotal))
                    }
                }
            }
            //actualizando el dinero actual siempre
            updateCurrentMoney(CurrentMoneyModel(money = dineroActual))
            sharedLogic._limitePorDia.value = sharedLogic.calcularLimitePorDia(dineroActual)
        }
    }

    private fun addDiner(cantidadIngresada: Double, dataTotal: Double): Double {
        Log.d("miApp", "fun agregarDinero  NUEVO VALOR = : $cantidadIngresada")
        Log.d("miApp", "fun agregarDinero  dataTotal = : $dataTotal")
        val resultado =
            BigDecimal(cantidadIngresada + dataTotal).setScale(2, RoundingMode.HALF_EVEN)
        Log.d("miApp", "fun agregarDinero = : $resultado")
        return resultado.toDouble()
    }

    private fun insertBaseDatos(item: CurrentMoneyModel) {
        // Agregar el valor a la base de datos
        viewModelScope.launch {
            val newValor = CurrentMoneyModel(money = item.money, isChecked = false)
            addCurrentMoneyUseCase(newValor)
            _dineroActual.value = getCurrentMoneyUseCase().money

        }
    }

    private fun insertPrimerIngresoProgress(item: TotalIngresosModel) {
        viewModelScope.launch {
            val primerIngreso = TotalIngresosModel(totalIngresos = item.totalIngresos)
            insertTotalIngresosUseCase(primerIngreso)
            _mostrandoDineroTotalIngresos.value = getTotalIngresosUseCase()?.totalIngresos
        }
    }

    private fun insertPrimerGastoProgress(item: TotalGastosModel) {
        viewModelScope.launch {
            val primerGasto = TotalGastosModel(totalGastos = item.totalGastos)
            insertTotalGastosUseCase(primerGasto)
            _mostrandoDineroTotalGastos.value = getTotalGastosUseCase()?.totalGastos
        }
    }

    private fun updateCurrentMoney(item: CurrentMoneyModel) {
        Log.d("miApp", "update dinero actual = ${item.money}")
        viewModelScope.launch {
            val newValor = getCurrentMoneyUseCase()
            newValor.money = item.money
            newValor.isChecked = false
            updateCurrentMoneyUseCase(newValor)
            mostrarDineroActual()
        }
    }

    private fun updateIngresoTotal(item: TotalIngresosModel) {
        Log.d("miApp", "item = fun updateIngresoTotal = ${item.totalIngresos}")
        viewModelScope.launch {
            val totalIngresosActual = getTotalIngresosUseCase()
            // Sumar solo el nuevo ingreso al total actual
            totalIngresosActual?.totalIngresos = item.totalIngresos
            // Actualizar el total de ingresos en el repositorio
            if (totalIngresosActual != null) {
                updateTotalIngresosUseCase(totalIngresosActual)
            }
            mostrarDineroActual()
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
        if (_dineroActual.value == 0.0) {
            _botonActivado.value = 1 // el boton ingreso esta activado
            _isChecked.value = true //elmenu de ingresos esta activado
            _enabledBotonGastos.value = false //elboton de gastos esta desactivado
        } else {
            _botonActivado.value = 0
            _isChecked.value = false
            _enabledBotonGastos.value = true
        }
    }

    fun onDialogClose() {
        _showDialogTransaction.value = false
        _cantidadIngresada.value = ""
        _description.value = ""
        // En el lugar donde cierras el diálogo
    }

    //obtiene el nuevo valor de la plata que agregamos
    private fun setCantidadIngresada(value: String) {
        _cantidadIngresada.value = value
    }

    private fun setDescription(nuevaDescripcion: String) {
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
        val day = SimpleDateFormat("dd", Locale.getDefault()).format(Date()).toInt()
        val formattedDay = String.format(Locale.getDefault(), "%02d", day)
        val dateFormat = SimpleDateFormat("$formattedDay MMM yyyy", Locale.getDefault())
        val fechaFormateada = dateFormat.format(Date())

        Log.d("fechaFormateada", fechaFormateada)
        viewModelScope.launch {
            addMovimientosUseCase(
                MovimientosModel(
                    iconResourceName = categoryIcon.toString(),
                    title = categoryName,
                    subTitle = description,
                    cash = cantidad,
                    select = isChecked,
                    date = fechaFormateada
                )
            )
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
        }
    }

    fun setShowNuevoMes(showNuevoMes: Boolean) {
        _showNuevoMes.value = showNuevoMes
    }
}