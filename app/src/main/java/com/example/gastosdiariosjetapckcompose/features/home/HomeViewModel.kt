package com.example.gastosdiariosjetapckcompose.features.home


import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastosdiariosjetapckcompose.SharedLogic
import com.example.gastosdiariosjetapckcompose.domain.model.CurrentMoneyModel
import com.example.gastosdiariosjetapckcompose.domain.model.FechaSaveModel
import com.example.gastosdiariosjetapckcompose.domain.model.MovimientosModel
import com.example.gastosdiariosjetapckcompose.features.movimientos.MovimientosViewModel
import com.example.gastosdiariosjetapckcompose.domain.model.TransaccionModel
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.GetFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.InsertFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.fechaElegida.UpdateFechaUsecase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.AddCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.GetCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.money.UpdateCurrentMoneyUseCase
import com.example.gastosdiariosjetapckcompose.domain.usecase.movimientos.AddMovimientosUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
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
    private val addMovimientosUseCase: AddMovimientosUsecase
) : ViewModel() {

    val sharedLogic = SharedLogic()

    private var _dineroActual = MutableLiveData<Double>()
    val dineroActual: LiveData<Double> = _dineroActual


    private var _limitePorDia = MutableLiveData<Double>(0.0)
    val limitePorDia: LiveData<Double> = _limitePorDia

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

    private val _isChecked = MutableLiveData<Boolean>()
    val isChecked: LiveData<Boolean> = _isChecked

    //variable para mostrar el dialogo de transaccion
    private val _showDialogTransaction = MutableLiveData<Boolean>()
    val showDialogTransaction: LiveData<Boolean> = _showDialogTransaction

    //variable para solo mostrar los dias restantes
    private val _diasRestantes = MutableLiveData<Int>(0)
    val diasRestantes: LiveData<Int> = _diasRestantes

    private val _selectedDateTrans = MutableLiveData<String>()
    val selectedDateTrans: LiveData<String> = _selectedDateTrans


    private val _fechaElegida = MutableLiveData<String>("Elige una fecha")
    val fechaElegida: LiveData<String> = _fechaElegida


    init {
        viewModelScope.launch {
            val getFecha = getFechaUsecase().fechaSave //contiene asi 2023-12-28
            val selectedDateTransValue = _selectedDateTrans.value
            if (selectedDateTransValue != null) {
                val fechaActual = LocalDate.now()
                val fechaParseada = LocalDate.parse(selectedDateTransValue)// 2023-12-12
                //si la fecha actual es superior que la fecha elegida
                if (fechaActual.isAfter(fechaParseada)) {
                    val nuevoMes = agregandoUnMes(fechaActual, fechaParseada)
                    updateFechaUsecase(FechaSaveModel(fechaSave = nuevoMes))
                    _fechaElegida.value = formateandofecha(getFecha)
                } else {
                    // Manejar el caso de que la fecha no sea posterior a la fecha elegida
                    // Por ejemplo, asignar un mensaje o realizar alguna acción
                }
            } else {
                // Manejar el caso de que _selectedDateTrans.value sea nulo
            }
            // Lógica relacionada con el dinero
            _dineroActual.value = getCurrentMoneyUseCase().money

            // Lógica relacionada con la fecha y otros valores
            _fechaElegida.value = formateandofecha(getFecha)
            _diasRestantes.value = diasRestantes(getFecha)
            _limitePorDia.value = calcularLimitePorDia()
        }
    }
    fun actualizarVistaDineroTotal(){
        viewModelScope.launch {
            _dineroActual.value = getCurrentMoneyUseCase().money
        }
    }
    fun actualizarVistaLimitePorDia(){
            _limitePorDia.value = calcularLimitePorDia()
    }

    fun setDateTransformer(year: Int, month: Int, day: Int) {
        val converter =
            "${year}-${String.format("%02d", month.plus(1))}-${String.format("%02d", day)}"
        //obtiene el valor de la fecha seleccionada asi
        //      2023-05-21
        _selectedDateTrans.value = converter
    }

    private fun insertUpdateFecha() {
        viewModelScope.launch {
            val nuevaFecha = _selectedDateTrans.value!!
            if (getFechaUsecase().isSelected) {
                insertandoFecha(FechaSaveModel(fechaSave = nuevaFecha, isSelected = false))
            } else {
                actualizandoFecha(FechaSaveModel(fechaSave = nuevaFecha))
            }
        }
    }

    private fun insertandoFecha(item: FechaSaveModel) {
        viewModelScope.launch {
            insertFechaUsecase(item)//insertando en la base de datos
            _selectedDateTrans.value = item.fechaSave
            mostrandoAlUsuario(item.fechaSave)
        }
    }

    private fun actualizandoFecha(item: FechaSaveModel) {
        viewModelScope.launch {
            // val nuevaFecha = getFechaUsecase().copy(fechaSave = item.fechaSave)
            val nuevaFecha = getFechaUsecase()
            nuevaFecha.fechaSave = item.fechaSave
            updateFechaUsecase(nuevaFecha)
            mostrandoAlUsuario(nuevaFecha.fechaSave)
        }
    }

    private fun mostrandoAlUsuario(fechaSave: String) {
        _fechaElegida.value = formateandofecha(fechaSave)
        _diasRestantes.value = diasRestantes(fechaSave)
        _limitePorDia.value = calcularLimitePorDia()
    }

    fun setDateTransformerconBarra(day: Int, month: Int, year: Int): String {
        val fechaBarra =
            "${String.format("%02d", day)}/${String.format("%02d", month.plus(1))}/${year}"
        // _fechaElegida.value = fechaBarra
        insertUpdateFecha()
        return fechaBarra
    }

    fun formateandofecha(fechaSave: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val localDate = LocalDate.parse(fechaSave, formatter)
            localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: DateTimeParseException) {
            // Manejar la excepción: devolver un mensaje alternativo o la misma cadena si no se puede analizar como fecha
            "Fecha inválida"
        }
    }

    private fun diasRestantes(getFecha: String): Int {
        val fechaActual: LocalDate = LocalDate.now()
        // val get = getFechaUsecase().fechaSave //tiene 2023-12-28
        val fLocalDate = LocalDate.parse(getFecha)
        var dia: Long = ChronoUnit.DAYS.between(fechaActual, fLocalDate)
        dia += 1
        return dia.toInt()
    }

    //     fun calculadora(nuevoValor: String) {
//         viewModelScope.launch {
//             // Obtener el valor de isChecked de manera síncrona
//              val    getCheked = getCurrentMoneyUseCase().isChecked
//                 Log.d("miApp", "getCheked : $getCheked")
//             // Realizar operaciones según el valor de isChecked
//             if (_isChecked.value == true) {
//                 //el usuario eligio ingreso
//                 val valorExistente = _dineroActual.value!!
//                 if (getCheked) {
//                     val nuevoDinero: Double = agregarDinero(nuevoValor.toDouble(), valorExistente)
//                     //importante para mostrar el calculo por dia
//                     _dineroActual.value = nuevoDinero
//                     Log.d("miApp", "Insertando: $nuevoDinero")
//                     //como no hay nada en la base de datos, inserta por primera vez en la base de datos
//                     insertBaseDatos(CurrentMoneyModel(money = nuevoDinero))
//                     //calcula el limite a gastar por dia luego del resultado
//                    calcularLimitePorDia()
//                 } else {
//                     //ingresa por segunda vez, ya no es nulo
//
//                     val resultadoSuma = agregarDinero(nuevoValor.toDouble(), _dineroActual.value!!)
//                     //importante para mostrar el calculo por dia
//                     _dineroActual.value = resultadoSuma
//                     //actualiza el numero guardado en la base de datos
//                     updateBaseDatos(CurrentMoneyModel(money = resultadoSuma))
//                     // updateBaseDatos(CurrentMoneyModel(money = resultadoSuma))
//                     Log.d("miApp", "Actualizando suma: $resultadoSuma")
//                    calcularLimitePorDia()
//                 }
//             } else {
//                 //el usuario eligio gastos
//                 val resultadoResta = restarDinero(nuevoValor)
//                 val nuevoDinero = if (resultadoResta < 0.0) 0.0 else resultadoResta
//                 //importante para mostrar el calculo por dia
//                 _dineroActual.value = nuevoDinero
//                 //actualiza el numero guardado en la base de datos
//                 updateBaseDatos(CurrentMoneyModel(money = nuevoDinero))
//                 Log.d("miApp", "Actualizando resta: $nuevoDinero")
//                 //calcula el limite a gastar por dia luego del resultado
//                calcularLimitePorDia()
//             }
//         }
//    }
    fun calculadora(nuevoValor: String) {
        viewModelScope.launch {
            try {
                // Obtener el valor de isChecked de manera síncrona
                val getChecked = getCurrentMoneyUseCase().isChecked
                Log.d("miApp", "getChecked: $getChecked")

                // Calcula el nuevo dinero según la opción elegida por el usuario
                val nuevoDinero = when (_isChecked.value) {
                    true -> agregarDinero(nuevoValor.toDouble(), _dineroActual.value ?: 0.0)
                    else -> maxOf(restarDinero(nuevoValor), 0.0)
                }

                // Actualiza el valor en _dineroActual.value y en la base de datos
                _dineroActual.value = nuevoDinero
                if (_isChecked.value == true && getChecked) {
                    insertBaseDatos(CurrentMoneyModel(money = nuevoDinero))
                } else {
                    updateBaseDatos(CurrentMoneyModel(money = nuevoDinero))
                }

                // Log y cálculo del límite por día
                val logMessage =
                    if (_isChecked.value == true && getChecked) "Insertando" else "Actualizando"
                Log.d("miApp", "$logMessage: $nuevoDinero")
//                _limitePorDia.value = calcularLimitePorDia()

                _limitePorDia.value = calcularLimitePorDia()

            } catch (e: NumberFormatException) {
                Log.e("miApp", "Error al convertir el valor a Double: $nuevoValor")
                // Manejar el error según sea necesario
            } catch (e: Exception) {
                Log.e("miApp", "Error inesperado: ${e.message}")
                // Manejar el error según sea necesario
            }
        }
    }

    private fun insertBaseDatos(item: CurrentMoneyModel) {
        // Agregar el valor a la base de datos
        viewModelScope.launch {
            val newValor = CurrentMoneyModel(money = item.money, isChecked = false)
            Log.d("miApp", "INSERTANDO : $newValor")
            addCurrentMoneyUseCase(newValor)
            _dineroActual.value = getCurrentMoneyUseCase().money
        }
    }

    private fun updateBaseDatos(item: CurrentMoneyModel) {
        viewModelScope.launch {
            val newValor = getCurrentMoneyUseCase()
            newValor.money = item.money
            Log.d("miApp", "ACTUALIZANDO : $newValor")
            updateCurrentMoneyUseCase(newValor)
            _dineroActual.value = getCurrentMoneyUseCase().money
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

    fun calcularLimitePorDia(): Double {
        val dineroActual = _dineroActual.value
        val limitePorDia = if (_diasRestantes.value != null && dineroActual != null && _diasRestantes.value != 0) {
                dineroActual / _diasRestantes.value!!.toDouble()
            } else {
                0.0
            }
        return limitePorDia
    }

    private fun agregandoUnMes(fechaActual: LocalDate, fechaParseada: LocalDate): String {
        var diff = ChronoUnit.MONTHS.between(fechaParseada, fechaActual)
        diff += 1
        val mes = fechaParseada.plus(diff, ChronoUnit.MONTHS)
        return mes.toString()
    }


    //metodo que devuelve un true para que abra el dialogo de transaccion
    fun onShowDialogClickTransaction() {
        _showDialogTransaction.value = true
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

    //metodo que transforma la moneda al pais correspondiente
    fun formattedCurrency(amount: Double): String {
        // val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        val currencyFormat = NumberFormat.getCurrencyInstance(java.util.Locale("es", "AR"))
        val formattedAmount = currencyFormat.format(amount)


        // currencyFormat.currency?.symbol
        //devuelve el simbolo de moneda correspondiente al pais del usuario
        // Elimina el símbolo de la moneda (en este caso, "$")
        val currencySymbol = currencyFormat.currency?.symbol ?: "$"
        return formattedAmount.replace(currencySymbol, "").trim()
    }
}



