package com.lukakordzaia.techincaltaskleavingstone.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukakordzaia.techincaltaskleavingstone.network.Result
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.FitnessInfo
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.MembersInfo
import com.lukakordzaia.techincaltaskleavingstone.repository.FitnessInfoRepository
import com.lukakordzaia.techincaltaskleavingstone.repository.MembersInfoRepository
import com.lukakordzaia.techincaltaskleavingstone.ui.BaseViewModel
import kotlinx.coroutines.launch

class MainFragmentViewModel : BaseViewModel() {
    private val repository = MembersInfoRepository()

    private val _fitnessInfo = MutableLiveData<FitnessInfo>()
    val fitnessInfo: LiveData<FitnessInfo> = _fitnessInfo

    private val totalMembers = MutableLiveData<String>()
    private val totalTime = MutableLiveData<String>()
    private val avgTime = MutableLiveData<String>()

    private val _clubInfo = MutableLiveData<List<String>>()
    val clubInfo: LiveData<List<String>> = _clubInfo

    private val _membersInfo = MutableLiveData<MutableList<MembersInfo.Member>>()
    val membersInfo: LiveData<MutableList<MembersInfo.Member>> = _membersInfo

    private val _chosenMember = MutableLiveData<MembersInfo.Member>()
    val chosenMember: LiveData<MembersInfo.Member> = _chosenMember

    private val _userInfo = MutableLiveData<FitnessInfo.Me>()
    val userInfo: LiveData<FitnessInfo.Me> = _userInfo

    private val _hasMore = MutableLiveData<Boolean>(true)
    val hasMore: LiveData<Boolean> = _hasMore

    private val membersList: MutableList<MembersInfo.Member> = ArrayList()

    fun onGroupOptionsPressed(){
        navigateToNewFragment(MainFragmentDirections.actionMainFragmentToGroupOptionsFragment())
    }

    fun getFitnessInfo(fitnessInfo: FitnessInfo) {
        val data = fitnessInfo
        _fitnessInfo.value = data

        if (data.info != null) {
            data.info.forEach {
                when (it.key) {
                    "წევრი" -> totalMembers.value = it.value ?: "N/A"
                    "საშ. დრო" -> avgTime.value = it.value ?: "N/A"
                    "სულ დრო" -> totalTime.value = it.value ?: "N/A"
                }
            }
            _clubInfo.value = listOf(totalMembers.value!!, avgTime.value!!, totalTime.value!!)
        }

//        viewModelScope.launch {
//            when (val fitnessInfo = repository.getFitnessInfo()) {
//                is Result.Success -> {
//                    val data = fitnessInfo.data
//                    _fitnessInfo.value = data
//
//                    if (data.info != null) {
//                        data.info.forEach {
//                            when (it.key) {
//                                "წევრი" -> totalMembers.value = it.value ?: "N/A"
//                                "საშ. დრო" -> avgTime.value = it.value ?: "N/A"
//                                "სულ დრო" -> totalTime.value = it.value ?: "N/A"
//                            }
//                        }
//                        _clubInfo.value = listOf(totalMembers.value!!, avgTime.value!!, totalTime.value!!)
//                    }
//                }
//                is Result.Error -> {
//                    Log.d("fitnessinfoerror", fitnessInfo.exception)
//                }
//            }
//        }
    }

    fun setChosenMember(member: MembersInfo.Member) {
        _chosenMember.value = member
    }

    fun getMembersInfo(page: Int) {
        if (hasMore.value == true) {
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
                        Log.d("memberslist", hasMore.value.toString())
                    }
                    is Result.Error -> {
                        Log.d("membersinfoerror", membersInfo.exception)
                    }
                }
            }
        } else {
            Log.d("currentpage", "no more")
        }
    }

    fun getUserInfo(userInfo: FitnessInfo.Me) {
        _userInfo.value = userInfo
    }
}