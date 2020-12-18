package com.lukakordzaia.techincaltaskleavingstone.network

import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.FitnessInfo
import com.lukakordzaia.techincaltaskleavingstone.network.datamodels.MembersInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiCalls {

    @GET("info.json")
    suspend fun getFitnessInfo() : Response<FitnessInfo>

    @GET("members/{page}.json")
    suspend fun getMembersInfo(@Path("page") page: Int ) : Response<MembersInfo>
}