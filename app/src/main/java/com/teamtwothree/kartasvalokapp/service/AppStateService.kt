package com.teamtwothree.kartasvalokapp.service

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.service.common.AppState
import org.kodein.di.generic.instance

class AppStateService {

    private val validationService: ValidationService by AppDelegate.getKodein().instance()
    private val dataService: DataService by AppDelegate.getKodein().instance()
    private val reportService: ReportService by AppDelegate.getKodein().instance()
    private val appService: AppStateService by AppDelegate.getKodein().instance()

    val appState = MutableLiveData<AppState>()

    init {
        val path =  "file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/171006-443-to7bN83pAcHC3CgI.JPG"
        reportService.newReport(listOf(Uri.parse(path)))
        generateReport()
    }

    fun generateReport() {
        appState.postValue(AppState.GENERATION)
    }

    fun validateReport() {
        appState.postValue(AppState.VALIDATION)
    }

    fun showHistory() {
        appState.postValue(AppState.HISTORY)
    }

}