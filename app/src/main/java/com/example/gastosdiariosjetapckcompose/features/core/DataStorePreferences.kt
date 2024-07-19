package com.example.gastosdiariosjetapckcompose.features.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gastosdiariosjetapckcompose.Constants.HORAS_PREDEFINIDAS
import com.example.gastosdiariosjetapckcompose.Constants.MINUTOS_PREDEFINIDOS
import com.example.gastosdiariosjetapckcompose.domain.model.FechaMaximaDataModel
import com.example.gastosdiariosjetapckcompose.domain.model.FechaMaximaDataModel.Companion.selectedOptionKey
import com.example.gastosdiariosjetapckcompose.domain.model.FechaMaximaDataModel.Companion.selectedSwitchOptionKey
import com.example.gastosdiariosjetapckcompose.domain.model.TimeDataModel
import com.example.gastosdiariosjetapckcompose.domain.model.TimeDataModel.Companion.selectedHourKey
import com.example.gastosdiariosjetapckcompose.domain.model.TimeDataModel.Companion.selectedMinuteKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class DataStorePreferences(private val context: Context) {

    //para asegurarse de que solo haya una instancia
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("FECHA_MAXIMA")

    // Claves para las preferencias de la hora y el minuto
    private val Context.selectedHourKey: DataStore<Preferences> by preferencesDataStore("SELECTED_HOUR")

    private val Context.viewPagerShow: DataStore<Preferences> by preferencesDataStore("VIEW_PAGER")
    private val showViewPagerKey = booleanPreferencesKey("SHOW_PAGER")


    //para recibir el dia maximo guardada
    fun getFechaMaximoMes() = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            FechaMaximaDataModel(
                numeroGuardado = preferences[selectedOptionKey] ?: "31",
                switchActivado = preferences[selectedSwitchOptionKey] ?: true
            )
        }

    //para guardar el dia maximo
    suspend fun setSelectedOption(option: String, selectedSwitchOption: Boolean) {
        context.dataStore.edit { preferences: MutablePreferences ->
            if (option != null) {
                preferences[selectedOptionKey] = option
                preferences[selectedSwitchOptionKey] = selectedSwitchOption
            } else {
                preferences.remove(selectedOptionKey)
                preferences.remove(selectedSwitchOptionKey)
            }
        }
    }


    // Recuperar la hora y el minuto seleccionados
    fun getHoraMinuto() = context.selectedHourKey.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            TimeDataModel(
                // Obtener la hora guardada, por defecto 21 si no hay valor
                hour = preferences[selectedHourKey] ?: HORAS_PREDEFINIDAS,
                // Obtener el minuto guardado, por defecto 0 si no hay valor
                minute = preferences[selectedMinuteKey] ?: MINUTOS_PREDEFINIDOS
            )
        }

    suspend fun setHoraMinuto(hour: Int?, minute: Int?) {
        context.selectedHourKey.edit { preferences ->
            if (hour != null) {
                preferences[selectedHourKey] = hour
            }
            if (minute != null) {
                preferences[selectedMinuteKey] = minute
            }
        }
    }

    fun getViewPager(): Flow<Boolean> = context.viewPagerShow.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // Obtener el valor de viewPagerShow, si no existe o es nulo, devolver false
            preferences[showViewPagerKey] ?: false
        }


    suspend fun setViewPagerShow(value: Boolean) {
        context.viewPagerShow.edit { preferences ->
            preferences[showViewPagerKey] = value
        }
    }
}
