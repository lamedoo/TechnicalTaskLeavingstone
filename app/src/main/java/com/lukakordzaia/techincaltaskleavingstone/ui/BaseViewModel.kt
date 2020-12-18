package com.lukakordzaia.techincaltaskleavingstone.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.FitnessInfo
import com.lukakordzaia.techincaltaskleavingstone.utils.Event

abstract class BaseViewModel : ViewModel() {
    private val _navigateScreen = MutableLiveData<Event<NavDirections>>()
    val navigateScreen: LiveData<Event<NavDirections>> = _navigateScreen

    fun navigateToNewFragment(navId: NavDirections) {
        _navigateScreen.value = Event(navId)
    }
}