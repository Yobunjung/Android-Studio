package com.eseul.yobunjeong.data

data class RecycleStatus(
    val success: Boolean,
    val message: String,
    val earnedPoints: Int = 0
)
