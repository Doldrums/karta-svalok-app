package com.teamtwothree.kartasvalokapp.presentation.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamtwothree.kartasvalokapp.R
import com.teamtwothree.kartasvalokapp.databinding.FrResultsBinding
import com.teamtwothree.kartasvalokapp.presentation.results.adapter.ReportsAdapter
import kotlinx.android.synthetic.main.fr_results.*

class ResultsFragment : Fragment(){

    private val resultsViewModel = ResultsViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FrResultsBinding = DataBindingUtil.inflate(inflater, R.layout.fr_results, container, false)
        binding.setLifecycleOwner(this)
        binding.vm = resultsViewModel
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler_results.adapter = ReportsAdapter(emptyList())
        recycler_results.layoutManager = LinearLayoutManager(this.activity)
    }
}