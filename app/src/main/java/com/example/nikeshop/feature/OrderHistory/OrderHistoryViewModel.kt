package com.example.nikeshop.feature.OrderHistory

import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.OrderHistoryItem
import com.example.nikeshop.data.repo.order.OrderRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest

class OrderHistoryViewModel(private val orderRepository: OrderRepository) : NikeViewModel() {
    val orderHistoryListLiveData = MutableLiveData<List<OrderHistoryItem>>()

    init {
        progressBarLiveData.value =true
        orderRepository.historyList().asyncNetworkRequest()
            .doOnSuccess{progressBarLiveData.value =false }
            .subscribe(object : NikeSingleObserver<List<OrderHistoryItem>>(compositeDisposable){

            override fun onSuccess(t: List<OrderHistoryItem>) {
                orderHistoryListLiveData.value = t
            }
        })
    }

}