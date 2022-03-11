package com.example.nikeshop.data.DataClass

data class CartItem(
    val cart_item_id: Int,
    var count: Int,
    val product: Product,
    var changeCountProgressBarVisible: Boolean = false
)