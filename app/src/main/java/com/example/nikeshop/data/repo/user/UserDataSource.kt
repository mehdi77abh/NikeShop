package com.example.nikeshop.data.repo.user

import com.example.nikeshop.data.DataClass.MessageResponse
import com.example.nikeshop.data.DataClass.TokenResponse
import io.reactivex.Completable
import io.reactivex.Single

interface UserDataSource {

    fun login(userName: String, password: String): Single<TokenResponse>

    fun signUp(userName: String, password: String): Single<MessageResponse>

    fun loadToken()

    fun saveToken(token: String, refreshToken: String)
    fun saveUserName(userName:String)
    fun loadUserName():String
    fun signOut()
}