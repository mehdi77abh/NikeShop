package com.example.nikeshop.feature.shipping

import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeView
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.repo.order.OrderRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import com.sevenlearn.nikestore.data.Checkout

class CheckoutViewModel(orderRepository: OrderRepository, orderId: Int) :
    NikeViewModel() {
    val checkoutLiveData = MutableLiveData<Checkout>()

    init {
        orderRepository.checkout(orderId).asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<Checkout>(compositeDisposable) {

                override fun onSuccess(t: Checkout) {
                    checkoutLiveData.value = t

                }
            })
    }

}