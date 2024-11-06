package com.eseul.yobunjeong.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeModel(
    @SerializedName("nickname")
    @Expose
    val nickname: String,

    @SerializedName("total_points")
    @Expose
    val points: Int,

    @SerializedName("recent_recycles")
    @Expose
    val recentRecycles: List<RecycleItem>,

    @SerializedName("recycle_counts")
    @Expose
    val recycleCounts: RecycleCounts
)

data class RecycleItem(
    @SerializedName("bin_type")
    @Expose
    val binType: String,

    @SerializedName("earned_points")
    @Expose
    val earnedPoints: Int,

    @SerializedName("timestamp")
    @Expose
    val timestamp: String
)

data class RecycleCounts(
    @SerializedName("can")
    @Expose
    val can: String,

    @SerializedName("paper")
    @Expose
    val paper: String,

    @SerializedName("plastic")
    @Expose
    val plastic: String
)
