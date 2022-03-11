package com.example.nikeshop.data.repo.user

import com.example.nikeshop.data.DataClass.TokenContainer
import com.example.nikeshop.data.DataClass.TokenResponse
import io.reactivex.Completable

class UserRepositoryImpl(
    val userLocalDataSource: UserDataSource,
    val userRemoteDataSource: UserDataSource
) : UserRepository {

    override fun login(userName: String, password: String): Completable {
        return userRemoteDataSource.login(userName, password).doOnSuccess {
            onSuccessfulLogin(userName,it)
        }.ignoreElement()
    }

    override fun signUp(userName: String, password: String): Completable {
        return userRemoteDataSource.signUp(userName,password).flatMap {
            userRemoteDataSource.login(userName, password)
        }.doOnSuccess {
        onSuccessfulLogin(userName,it)
        }.ignoreElement()
    }

    override fun loadToken() {
        userLocalDataSource.loadToken()
    }

    override fun loadUserName(): String =userLocalDataSource.loadUserName()

    override fun signOut() {
        userLocalDataSource.signOut()
        TokenContainer.update(null,null)
    }
    private fun onSuccessfulLogin(userName: String,tokenResponse: TokenResponse){
        TokenContainer.update(tokenResponse.access_token,tokenResponse.refresh_token)
        userLocalDataSource.saveToken(tokenResponse.access_token,tokenResponse.refresh_token)
        userLocalDataSource.saveUserName(userName)

    }


}
