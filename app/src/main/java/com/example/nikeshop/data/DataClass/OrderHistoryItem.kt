package com.example.nikeshop.data.DataClass

data class OrderHistoryItem(
    val id: Int,
    val payable: Int,
    val order_items: List<OrderItem>,

    )