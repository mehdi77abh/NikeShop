package com.example.nikeshop.data.repo.user

import com.example.nikeshop.data.DataClass.MessageResponse
import com.example.nikeshop.data.DataClass.TokenResponse
import com.example.nikeshop.serivce.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

const val CLIENT_ID = 2
const val CLIENT_SECRET = "kyj1c9sVcksqGU4scMX7nLDalkjp2WoqQEf8PKAC"

class UserRemoteDataSource(val apiService: ApiService) : UserDataSource {

    override fun login(userName: String, password: String): Single<TokenResponse> {
        return apiService.login(JsonObject().apply {
            addProperty("username", userName)
            addProperty("password", password)
            addProperty("grant_type", "password")
            addProperty("client_id", CLIENT_ID)
            addProperty("client_secret", CLIENT_SECRET)
        })
    }

    override fun signUp(userName: String, password: String): Single<MessageResponse> {
        return apiService.signUp(JsonObject().apply {
            addProperty("email", userName)
            addProperty("password", password)

        })
    }

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun loadUserName(): String {
        TODO("Not yet implemented")
    }

    override fun saveUserName(userName: String) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}