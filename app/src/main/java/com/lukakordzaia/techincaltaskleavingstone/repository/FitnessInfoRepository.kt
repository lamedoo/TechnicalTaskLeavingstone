package com.lukakordzaia.techincaltaskleavingstone.repository

import com.lukakordzaia.techincaltaskleavingstone.network.ApiCalls
import com.lukakordzaia.techincaltaskleavingstone.network.Result
import com.lukakordzaia.techincaltaskleavingstone.network.RetrofitBuilder
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.FitnessInfo
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.MembersInfo

class FitnessInfoRepository {
    private val retrofit = RetrofitBuilder.buildRetrofit(ApiCalls::class.java)

    suspend fun getFitnessInfo() : Result<FitnessInfo> {
        return RetrofitBuilder.retrofitCall { retrofit.getFitnessInfo() }
    }

    suspend fun getMembersInfo(page: Int): Result<MembersInfo> {
        return RetrofitBuilder.retrofitCall { retrofit.getMembersInfo(page) }
    }
}