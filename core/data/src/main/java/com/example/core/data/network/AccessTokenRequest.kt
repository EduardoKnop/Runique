package com.example.core.data.network

data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String
)
