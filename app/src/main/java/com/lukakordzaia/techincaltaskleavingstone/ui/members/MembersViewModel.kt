package com.lukakordzaia.techincaltaskleavingstone.ui.members

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

class MembersViewModel : BaseViewModel() {
    private val repository = FitnessInfoRepository()

    private val _membersInfo = MutableLiveData<MutableList<MembersInfo.Member>>()
    val membersInfo: LiveData<MutableList<MembersInfo.Member>> = _membersInfo

    private val _chosenMember = MutableLiveData<MembersInfo.Member>()
    val chosenMember: LiveData<MembersInfo.Member> = _chosenMember

    private val _userInfo = MutableLiveData<FitnessInfo.Me>()
    val userInfo: LiveData<FitnessInfo.Me> = _userInfo

    private val _hasMore = MutableLiveData<Boolean>(true)
    val hasMore: LiveData<Boolean> = _hasMore

    private val membersList: MutableList<MembersInfo.Member> = ArrayList()

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

    fun getUserInfo() {
        viewModelScope.launch {
            when (val userInfo = repository.getFitnessInfo()) {
                is Result.Success -> {
                    val data = userInfo.data.me
                    _userInfo.value = data
                }
            }
        }
    }
}