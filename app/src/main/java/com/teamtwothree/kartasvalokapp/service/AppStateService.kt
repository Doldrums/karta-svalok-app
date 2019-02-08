package com.teamtwothree.kartasvalokapp.service

import androidx.lifecycle.MutableLiveData
import com.teamtwothree.kartasvalokapp.service.common.AppState

class AppStateService {
    val appState = MutableLiveData<AppState>()

    fun generateReport() {
        appState.postValue(AppState.GENERATION)
    }

    fun validateReport() {
        appState.postValue(AppState.VALIDATION)
    }

}