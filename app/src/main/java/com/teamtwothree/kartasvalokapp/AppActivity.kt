package com.teamtwothree.kartasvalokapp

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.FirebaseApp
import com.teamtwothree.kartasvalokapp.presentation.validation.ValidationFragment
import com.teamtwothree.kartasvalokapp.service.DataService
import com.teamtwothree.kartasvalokapp.service.ReportService
import com.teamtwothree.kartasvalokapp.service.ValidationService
import org.kodein.di.generic.instance
import com.teamtwothree.kartasvalokapp.presentation.generation.GeneratorFragment
import com.teamtwothree.kartasvalokapp.presentation.results.ResultsFragment
import com.teamtwothree.kartasvalokapp.service.AppStateService
import com.teamtwothree.kartasvalokapp.service.common.AppState


class AppActivity : AppCompatActivity() {


    private val validationService: ValidationService by AppDelegate.getKodein().instance()
    private val dataService: DataService by AppDelegate.getKodein().instance()
    private val reportService: ReportService by AppDelegate.getKodein().instance()
    private val appService: AppStateService by AppDelegate.getKodein().instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        FirebaseApp.initializeApp(this)

        appService.appState.observe(this, Observer { changeAppState(it) })
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment).commit()
    }

    fun changeAppState(state: AppState) =
            when (state) {
                AppState.GENERATION -> replaceFragment(GeneratorFragment())
                AppState.VALIDATION -> replaceFragment(ValidationFragment())
                AppState.HISTORY -> replaceFragment(ResultsFragment())
            }

}
