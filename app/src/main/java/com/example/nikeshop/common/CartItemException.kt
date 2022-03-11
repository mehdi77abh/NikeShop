package com.example.nikeshop.common

import androidx.annotation.StringRes

class CartItemException(val type: NikeException.Type, @StringRes val resourceStringMessage: Int,) : Exception() {

}