package com.example.nikeshop.data.DataClass

import timber.log.Timber

object TokenContainer {
    var token: String? = null
        private set
    var refreshToken: String? = null
        private set


    fun update(token: String?, refreshToken: String?) {
        Timber.i("Access Token = ${token?.substring(0, 10)} && \n${refreshToken?.substring(0, 10)}")
        this.token = token
        this.refreshToken = refreshToken

    }


}
