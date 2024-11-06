package com.eseul.yobunjeong.model

import com.google.gson.annotations.SerializedName

data class PointModel(
    @SerializedName("total_points") val totalPoints: Int,
    @SerializedName("points_logs") val pointsLogs: List<PointLog>
)

data class PointLog(
    @SerializedName("points_change") val pointsChange: Int,
    @SerializedName("timestamp") val timestamp: String
)
