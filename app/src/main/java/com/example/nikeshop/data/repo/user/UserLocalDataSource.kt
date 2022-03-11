package com.example.nikeshop.data.repo.user

import android.content.SharedPreferences
import com.example.nikeshop.data.DataClass.MessageResponse
import com.example.nikeshop.data.DataClass.TokenContainer
import com.example.nikeshop.data.DataClass.TokenResponse
import io.reactivex.Single

class UserLocalDataSource(val sharedPreferences: SharedPreferences) : UserDataSource {

    override fun login(userName: String, password: String): Single<TokenResponse> {
        TODO("Not yet implemented")
    }

    override fun signUp(userName: String, password: String): Single<MessageResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.update(
            sharedPreferences.getString("access_token", null),
            sharedPreferences.getString("refresh_token", null)
        )

    }

    override fun saveToken(token: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", token)
            putString("refresh_token", refreshToken)
        }.apply()
    }

    override fun loadUserName(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    override fun signOut() {
       sharedPreferences.edit().apply {
         clear()
       }.apply()
    }

    override fun saveUserName(userName: String) {
        sharedPreferences.edit().apply {
            putString("username", userName)
        }.apply()
    }
}