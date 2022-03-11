package com.example.nikeshop.data.repo.order

import com.example.nikeshop.data.DataClass.OrderHistoryItem
import com.sevenlearn.nikestore.data.Checkout
import com.sevenlearn.nikestore.data.SubmitOrderResult
import io.reactivex.Single

class OrderRepositoryImpl(val orderRemoteDataSource: OrderDataSource) : OrderRepository {


    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> = orderRemoteDataSource.submit(
        firstName,
        lastName,
        postalCode,
        phoneNumber,
        address,
        paymentMethod
    )

    override fun checkout(orderId: Int): Single<Checkout> = orderRemoteDataSource.checkout(orderId)
    override fun historyList(): Single<List<OrderHistoryItem>> =
        orderRemoteDataSource.historyList()
}