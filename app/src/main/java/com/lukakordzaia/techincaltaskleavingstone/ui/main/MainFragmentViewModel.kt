package com.lukakordzaia.techincaltaskleavingstone.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukakordzaia.techincaltaskleavingstone.network.Result
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.FitnessInfo
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.MembersInfo
import com.lukakordzaia.techincaltaskleavingstone.repository.FitnessInfoRepository
import com.lukakordzaia.techincaltaskleavingstone.ui.BaseViewModel
import kotlinx.coroutines.launch

class MainFragmentViewModel : BaseViewModel() {
    private val repository = FitnessInfoRepository()

    private val _fitnessInfo = MutableLiveData<FitnessInfo>()
    val fitnessInfo: LiveData<FitnessInfo> = _fitnessInfo

    private var totalMembers = ""
    private var totalTime = ""
    private var avgTime = ""

    private val _clubInfo = MutableLiveData<List<String>>()
    val clubInfo: LiveData<List<String>> = _clubInfo

    private val _membersInfo = MutableLiveData<MutableList<MembersInfo.Member>>()
    val membersInfo: LiveData<MutableList<MembersInfo.Member>> = _membersInfo

    private val _chosenMember = MutableLiveData<MembersInfo.Member>()
    val chosenMember: LiveData<MembersInfo.Member> = _chosenMember

    private val _userInfo = MutableLiveData<FitnessInfo.Me>()
    val userInfo: LiveData<FitnessInfo.Me> = _userInfo

    private val _hasMore = MutableLiveData(true)
    val hasMore: LiveData<Boolean> = _hasMore

    private val membersList: MutableList<MembersInfo.Member> = ArrayList()

    fun onGroupOptionsPressed(){
        navigateToNewFragment(MainFragmentDirections.actionMainFragmentToGroupOptionsFragment())
    }

    fun getFitnessInfo() {
        viewModelScope.launch {
            when (val fitnessInfo = repository.getFitnessInfo()) {
                is Result.Success -> {
                    val data = fitnessInfo.data
                    _fitnessInfo.value = data

                    if (data.info != null) {
                        setFitnessInfo(data.info)
                    }
                    if (data.me != null) {
                        _userInfo.value = data.me
                    }

                    showProgressBar(false)
                }
                is Result.Error -> {
                    Log.d("fitnessinfoerror", fitnessInfo.exception)
                    showProgressBar(true)
                }
                is Result.Internet -> {
                    newToastMessage("შეამოწმეთ ინტერნეტთან კავშირი")
                }
            }
        }
    }

    private fun setFitnessInfo(data: List<FitnessInfo.Info>) {
        data.forEach { key ->
            when (key.key) {
                "წევრი" -> {
                    val value = key.value?.filter { it.isDigit() }
                    totalMembers = value ?: "N/A"
                }
                "საშ. დრო" -> {
                    val value = key.value?.filter { it.isDigit() }
                    avgTime = value ?: "N/A"
                }
                "სულ დრო" -> {
                    val value = key.value?.filter { it.isDigit() }
                    totalTime = value ?: "N/A"
                }
            }
        }
        _clubInfo.value = listOf(totalMembers, avgTime, totalTime)
    }

    fun setChosenMember(member: MembersInfo.Member) {
        _chosenMember.value = member
    }

    fun getMembersInfo(page: Int) {
        if (_hasMore.value == true) {
            viewModelScope.launch {
                when (val membersInfo = repository.getMembersInfo(page)) {
                    is Result.Success -> {
                        val data = membersInfo.data
                        if (!data.members.isNullOrEmpty()) {
                            data.members.forEach {
                                membersList.add(it)
                            }
                            _membersInfo.value = membersList
                        }
                        _hasMore.value = data.hasMore
                    }
                    is Result.Error -> {
                        Log.d("membersinfoerror", membersInfo.exception)
                    }
                    is Result.Internet -> {
                        newToastMessage("შეამოწმეთ ინტერნეტთან კავშირი")
                    }
                }
            }
        } else {
            Log.d("currentpage", "no more")
        }
    }
}