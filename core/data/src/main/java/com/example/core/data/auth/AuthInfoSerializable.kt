package com.example.core.data.auth

data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
