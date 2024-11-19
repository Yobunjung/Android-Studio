package com.eseul.yobunjeong.model
data class BinStatusModel(
    val address: String,       // 주소 정보
    val bins: List<Bin>,       // 쓰레기통 상태 리스트
    val set_id: Int            // 세트 ID
)

data class Bin(
    val status: String,        // 상태 (on, full 등)
    val trash_type: String     // 쓰레기 종류 (Paper, Plastic, Can 등)
)

