package com.example.gastosdiariosjetapckcompose.features.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gastosdiariosjetapckcompose.domain.model.FechaMaximaDataModel
import com.example.gastosdiariosjetapckcompose.domain.model.FechaMaximaDataModel.Companion.selectedOptionKey
import com.example.gastosdiariosjetapckcompose.domain.model.FechaMaximaDataModel.Companion.selectedSwitchOptionKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map


class DataStorePreferences(private val context: Context) {
    //para asegurarse de que solo haya una instancia
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("FECHA_MAXIMA")

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
}