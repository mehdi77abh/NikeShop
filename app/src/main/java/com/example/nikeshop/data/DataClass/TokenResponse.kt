package com.example.nikeshop.data.DataClass

data class TokenResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val token_type: String
)