package com.example.nikeshop.feature.shipping

import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.data.repo.order.OrderRepository
import com.sevenlearn.nikestore.data.SubmitOrderResult
import io.reactivex.Single
const val PAYMENT_METHOD_COD = "cash_on_delivery"
const val PAYMENT_METHOD_ONLINE = "online"
class ShippingViewModel(private val orderRepository: OrderRepository) : NikeViewModel() {

    fun submit(
        firstName: String,
        lastName: String,
        postalCode: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String
    ): Single<SubmitOrderResult> =
        orderRepository.submit(firstName, lastName, postalCode, phoneNumber, address, paymentMethod)

}