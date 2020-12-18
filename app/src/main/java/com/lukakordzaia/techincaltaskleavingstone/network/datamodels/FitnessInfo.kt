package com.lukakordzaia.techincaltaskleavingstone.network.datamodels


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FitnessInfo(
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("info")
    val info: List<Info>?,
    @SerializedName("me")
    val me: Me?,
    @SerializedName("name")
    val name: String?
): Parcelable {
    @Parcelize
    data class Info(
        @SerializedName("key")
        val key: String?,
        @SerializedName("value")
        val value: String?
    ): Parcelable

    @Parcelize
    data class Me(
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