package com.example.nikeshop.common

import com.example.nikeshop.R
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception
import java.net.SocketTimeoutException
import java.util.*

class NikeExceptionMapper {
    companion object {
        fun map(throwable: Throwable): NikeException {
            when (throwable) {
                is HttpException -> {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    val errorMessage = errorJsonObject.getString("message")

                    when (throwable.code()) {
                        401 -> {
                            return if (errorMessage == "The user credentials were incorrect.") NikeException(
                                NikeException.Type.SIMPLE,
                                userFriendlyMessage = R.string.usernameOrPasswordIsIncorrect
                            )
                            else NikeException(
                                NikeException.Type.AUTH,
                                serverMessage = errorMessage
                            )
                        }
                        422 -> {
                            return NikeException(NikeException.Type.SIMPLE, serverMessage = errorMessage)


                        }
                        400 ->return NikeException(NikeException.Type.SIMPLE,R.string.usernameOrPasswordCantBeNull)

                    }

                }
                is CartItemException ->{
                    return NikeException(throwable.type,userFriendlyMessage = throwable.resourceStringMessage)

                }
                is SocketTimeoutException ->{
                    return NikeException(NikeException.Type.INTERNET_CONNECTION,R.string.connectionLost)

                }
            }


            return NikeException(NikeException.Type.SIMPLE, R.string.unknownError)
        }
    }
}