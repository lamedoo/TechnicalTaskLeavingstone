package com.lukakordzaia.techincaltaskleavingstone.ui.progress

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lukakordzaia.techincaltaskleavingstone.R
import com.lukakordzaia.techincaltaskleavingstone.utils.EventObserver
import com.lukakordzaia.techincaltaskleavingstone.utils.navController

class ProgressFragment : Fragment(R.layout.fragment_progress) {
    private lateinit var viewModel: ProgressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProgressViewModel::class.java)
        viewModel.getFitnessInfo()

        viewModel.fitnessInfo.observe(viewLifecycleOwner, Observer { fitnessInfo ->
                viewModel.onDataLoaded(fitnessInfo)
        })

        viewModel.navigateScreen.observe(viewLifecycleOwner, EventObserver {
            navController(it)
        })
    }
}