package com.example.nikeshop.feature.Auth

import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.data.repo.user.UserRepository
import io.reactivex.Completable

class AuthViewModel(private val userRepository: UserRepository) : NikeViewModel() {

    fun login(email: String, password: String): Completable {
        progressBarLiveData.value = true
        return userRepository.login(email,password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
    fun signUp(email: String,password: String):Completable{
        progressBarLiveData.value = true
        return  userRepository.signUp(email,password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }

}