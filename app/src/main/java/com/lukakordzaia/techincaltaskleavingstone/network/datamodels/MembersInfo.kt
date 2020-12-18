package com.lukakordzaia.techincaltaskleavingstone.network.datamodels


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MembersInfo(
    @SerializedName("hasMore")
    val hasMore: Boolean?,
    @SerializedName("members")
    val members: List<Member>?
): Parcelable {
    @Parcelize
    data class Member(
        @SerializedName("hours")
        val hours: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("imageUrl")
        val imageUrl: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("position")
        val position: Int?
    ): Parcelable
}