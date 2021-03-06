package com.example.nikeshop.data.repo.user

import io.reactivex.Completable

interface UserRepository {

    fun login(userName: String, password: String): Completable

    fun signUp(userName: String, password: String): Completable

    fun loadToken()

    fun loadUserName():String

    fun signOut()


}