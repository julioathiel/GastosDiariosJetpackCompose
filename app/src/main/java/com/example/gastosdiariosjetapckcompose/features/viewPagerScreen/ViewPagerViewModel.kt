package com.example.gastosdiariosjetapckcompose.features.viewPagerScreen

import androidx.lifecycle.ViewModel
import com.example.gastosdiariosjetapckcompose.features.core.DataStorePreferences
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