package com.example.nikeshop.data.DataClass

import com.example.nikeshop.data.DataClass.Product

data class OrderItem(
    val count: Int,
    val discount: Int,
    val id: Int,
    val order_id: Int,
    val price: Int,
    val product: Product,
    val product_id: Int
)