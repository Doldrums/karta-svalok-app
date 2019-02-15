package com.teamtwothree.kartasvalokapp.presentation.results

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.model.point.PointDetails
import com.teamtwothree.kartasvalokapp.service.DataService
import org.kodein.di.generic.instance

class ResultsViewModel: ViewModel() {

    private val dataService: DataService by AppDelegate.getKodein().instance()
    val reports: LiveData<List<PointDetails>> = dataService.getAllPointDetails()

}