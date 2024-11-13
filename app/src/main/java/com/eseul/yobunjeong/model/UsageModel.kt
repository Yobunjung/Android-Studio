package com.eseul.yobunjeong.model

import com.google.gson.annotations.SerializedName

// 개별 재활용 기록을 담는 클래스
data class RecentRecycle(
    // @SerializedName("earned_points")
    // val earnedPoints: Int,
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("trash_type")
    val trashType: String
)

// 전체 데이터를 감싸는 모델
data class UsageModel(
    @SerializedName("recent_recycles")
    val recentRecycles: List<RecentRecycle>, // 최근 재활용 기록 리스트

    @SerializedName("recycle_counts")
    val recycleCounts: RecycleCountDetails // 재활용 횟수 정보
)

// 재활용 횟수를 담는 클래스
data class RecycleCountDetails(
    @SerializedName("Can")
    val can: String,
    @SerializedName("Paper")
    val paper: String,
    @SerializedName("Plastic")
    val plastic: String
)
