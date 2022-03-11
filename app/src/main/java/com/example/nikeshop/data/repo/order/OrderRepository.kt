package com.example.nikeshop.data.repo.order

import com.example.nikeshop.data.DataClass.OrderHistoryItem
import com.sevenlearn.nikestore.data.Checkout
import com.sevenlearn.nikestore.data.SubmitOrderResult
import io.reactivex.Single

interface OrderRepository {

    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod:String
    ):Single<SubmitOrderResult>

    fun checkout(orderId:Int):Single<Checkout>

    fun historyList():Single<List<OrderHistoryItem>>

}