package com.teamtwothree.kartasvalokapp.presentation.generation

import androidx.lifecycle.ViewModel
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.service.ReportService
import org.kodein.di.generic.instance

class GeneratorViewModel: ViewModel() {
    private val generatorService: ReportService by AppDelegate.getKodein().instance()
    val generatorState = generatorService.getState()
}