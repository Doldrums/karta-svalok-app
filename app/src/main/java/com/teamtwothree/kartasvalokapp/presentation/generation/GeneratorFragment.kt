package com.teamtwothree.kartasvalokapp.presentation.generation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.teamtwothree.kartasvalokapp.AppActivity
import com.teamtwothree.kartasvalokapp.AppDelegate
import com.teamtwothree.kartasvalokapp.R
import com.teamtwothree.kartasvalokapp.databinding.FrGenerateReportBinding
import com.teamtwothree.kartasvalokapp.service.AppStateService
import com.teamtwothree.kartasvalokapp.service.common.OperationState
import org.kodein.di.generic.instance

class GeneratorFragment: DialogFragment() {

    private val appStateService: AppStateService by AppDelegate.getKodein().instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FrGenerateReportBinding = DataBindingUtil.inflate(inflater, R.layout.fr_generate_report, container, false)
        binding.setLifecycleOwner(this)
        val viewModel = ViewModelProviders.of(this).get(GeneratorViewModel::class.java)
        binding.vm = viewModel
        viewModel.generatorState.observe(this, Observer {
            if (it != null && it != OperationState.VALIDATING) {
                appStateService.validateReport()
            }
        })
        return binding.root
    }
}