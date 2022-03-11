package com.example.nikeshop.data.repo.order

import com.example.nikeshop.data.DataClass.OrderHistoryItem
import com.example.nikeshop.serivce.http.ApiService
import com.google.gson.JsonObject
import com.sevenlearn.nikestore.data.Checkout
import com.sevenlearn.nikestore.data.SubmitOrderResult
import io.reactivex.Single

class OrderRemoteDataSource(val apiService: ApiService) : OrderDataSource {
    override fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> = apiService.submitOrder(JsonObject().apply {
            addProperty("first_name", firstName)
            addProperty("last_name", lastName)
            addProperty("postal_code", postalCode)
            addProperty("mobile", phoneNumber)
            addProperty("address", address)
            addProperty("payment_method", paymentMethod)
        })


    override fun checkout(orderId: Int): Single<Checkout> =apiService.checkout(orderId)


    override fun historyList(): Single<List<OrderHistoryItem>> =apiService.getOrderHistory()
}