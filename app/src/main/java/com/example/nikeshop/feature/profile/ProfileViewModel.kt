package com.example.nikeshop.feature.profile

import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.data.DataClass.TokenContainer
import com.example.nikeshop.data.repo.user.UserRepository

class ProfileViewModel(private val userRepository: UserRepository):NikeViewModel() {
    val userName:String
    get() = userRepository.loadUserName()

    val isSignedIn:Boolean
    get() = TokenContainer.token!=null

    fun signOut() = userRepository.signOut()




}