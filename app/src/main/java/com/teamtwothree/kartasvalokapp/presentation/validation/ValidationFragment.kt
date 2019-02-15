package com.teamtwothree.kartasvalokapp.presentation.validation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.teamtwothree.kartasvalokapp.R
import com.teamtwothree.kartasvalokapp.databinding.FrValidationBinding

class ValidationFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FrValidationBinding = DataBindingUtil.inflate(inflater, R.layout.fr_validation, container, false)
        binding.lifecycleOwner = this
        binding.vm = ViewModelProviders.of(this).get(ValidationViewModel::class.java)
        return binding.root
    }
}