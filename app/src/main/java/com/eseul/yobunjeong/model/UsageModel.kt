package com.eseul.yobunjeong.model

import com.google.gson.annotations.SerializedName

data class UsageModel(
    @SerializedName("bin_type") val binType: String,
    @SerializedName("earned_points") val earnedPoints: Int,
    @SerializedName("recycle_count") val recycleCount: Int,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("user_id") val userId: Int
)
