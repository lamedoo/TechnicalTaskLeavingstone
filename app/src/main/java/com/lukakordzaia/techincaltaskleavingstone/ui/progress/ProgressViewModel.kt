package com.lukakordzaia.techincaltaskleavingstone.ui.progress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukakordzaia.techincaltaskleavingstone.network.Result
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.FitnessInfo
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.MembersInfo
import com.lukakordzaia.techincaltaskleavingstone.repository.FitnessInfoRepository
import com.lukakordzaia.techincaltaskleavingstone.ui.BaseViewModel
import com.lukakordzaia.techincaltaskleavingstone.ui.main.MainFragmentDirections
import kotlinx.coroutines.launch

class ProgressViewModel : BaseViewModel() {
    private val repository = FitnessInfoRepository()

    private val _fitnessInfo = MutableLiveData<FitnessInfo>()
    val fitnessInfo: LiveData<FitnessInfo> = _fitnessInfo

    private val membersList: MutableList<MembersInfo.Member> = ArrayList()

    private val _membersInfo = MutableLiveData<List<MembersInfo.Member>>()
    val membersInfo: LiveData<List<MembersInfo.Member>> = _membersInfo


    fun onDataLoaded(fitnessInfo: FitnessInfo) {
        navigateToNewFragment(ProgressFragmentDirections.actionProgressFragmentToMainFragment(fitnessInfo))
    }

    fun getFitnessInfo() {
        viewModelScope.launch {
            when (val fitnessInfo = repository.getFitnessInfo()) {
                is Result.Success -> {
                    val data = fitnessInfo.data
                    _fitnessInfo.value = data
                }
                is Result.Error -> {
                    Log.d("fitnessinfoerror", fitnessInfo.exception)
                }
            }
        }
    }

    fun getMembersInfo() {
        viewModelScope.launch {
            when (val membersInfo = repository.getMembersInfo(1)) {
                is Result.Success -> {
                    val data = membersInfo.data
                    if (!data.members.isNullOrEmpty()) {
                        data.members.forEach {
                            membersList.add(it)
                        }
                        _membersInfo.value = membersList
                    }
                }
                is Result.Error -> {
                    Log.d("membersinfoerror", membersInfo.exception)
                }
            }
        }
    }
}