package com.lukakordzaia.techincaltaskleavingstone.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.lukakordzaia.techincaltaskleavingstone.utils.Event

abstract class BaseViewModel : ViewModel() {
    private val _navigateScreen = MutableLiveData<Event<NavDirections>>()
    val navigateScreen: LiveData<Event<NavDirections>> = _navigateScreen

    private val _showProgress = MutableLiveData<Event<Boolean>>()
    val showProgress: LiveData<Event<Boolean>> = _showProgress

    private val _toastMessage = MutableLiveData<Event<String>>()
    val toastMessage: LiveData<Event<String>> = _toastMessage

    fun newToastMessage(message: String) {
        _toastMessage.value = Event(message)
    }

    fun navigateToNewFragment(navId: NavDirections) {
        _navigateScreen.value = Event(navId)
    }

    fun showProgressBar(isLoading: Boolean) {
        _showProgress.value = Event(isLoading)
    }
}