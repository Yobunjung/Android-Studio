package com.eseul.yobunjeong.model

// 로그인 요청 데이터를 위한 데이터 클래스
data class LoginRequest(
    val email: String,
    val password: String
)

// 로그인 응답 데이터를 위한 데이터 클래스
data class LoginResponse(
    val message: String,
    val user_name: String
)
