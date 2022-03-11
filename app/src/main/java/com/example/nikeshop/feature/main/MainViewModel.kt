package com.example.nikeshop.feature.main

import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.CartItemCount
import com.example.nikeshop.data.DataClass.TokenContainer
import com.example.nikeshop.data.repo.cart.CartRepository
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val cartRepository: CartRepository) : NikeViewModel() {

    fun getCartItemCounts() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            cartRepository.getCartItemsCount().subscribeOn(Schedulers.io())
                .subscribe(object : NikeSingleObserver<CartItemCount>(compositeDisposable) {
                    override fun onSuccess(t: CartItemCount) {

                        EventBus.getDefault().postSticky(t)


                    }


                })


        }
    }
}