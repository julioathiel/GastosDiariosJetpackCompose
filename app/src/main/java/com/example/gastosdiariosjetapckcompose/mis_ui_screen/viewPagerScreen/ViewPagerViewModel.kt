package com.example.gastosdiariosjetapckcompose.mis_ui_screen.viewPagerScreen

import androidx.lifecycle.ViewModel
import com.example.gastosdiariosjetapckcompose.data.core.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewPagerViewModel @Inject constructor(private val dataStorePreferences: DataStorePreferences) :
    ViewModel() {

    fun showViewPager(): Flow<Boolean> {
        return dataStorePreferences.getViewPager()

    }

    suspend fun setShowViewPager(isChecked: Boolean) {
        dataStorePreferences.setViewPagerShow(isChecked)
    }
}