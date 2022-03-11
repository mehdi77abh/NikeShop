package com.example.nikeshop.feature.cart

import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.R
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.*
import com.example.nikeshop.data.repo.cart.CartRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.Completable
import org.greenrobot.eventbus.EventBus

class CartViewModel(val cartRepository: CartRepository) : NikeViewModel() {
    val cartItemLiveData = MutableLiveData<List<CartItem>>()
    val purchaseDetailLiveData = MutableLiveData<PurchaseDetail>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()
    private fun getCartItems() {
        if (!TokenContainer.token.isNullOrEmpty()) {
            progressBarLiveData.value = true
            emptyStateLiveData.value = EmptyState(false)
            cartRepository.get().asyncNetworkRequest()
                .doOnSuccess { progressBarLiveData.value = false }
                .subscribe(object : NikeSingleObserver<CartResponse>(compositeDisposable) {
                    override fun onSuccess(t: CartResponse) {
                        if (t.cart_items.isNotEmpty()) {
                            cartItemLiveData.value = t.cart_items
                            purchaseDetailLiveData.value =
                                PurchaseDetail(t.total_price, t.shipping_cost, t.payable_price)

                        } else
                            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyState)
                    }


                })
        } else
            emptyStateLiveData.value = EmptyState(true, R.string.cartEmptyStateLoginRequired, true)

    }

    fun removeItemFromCart(cartItem: CartItem): Completable {
        return cartRepository.remove(cartItem.cart_item_id).doAfterSuccess {
            calculateAndPublishPurchaseDetail()

            val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
            cartItemCount?.let {
                it.count -= cartItem.count
                EventBus.getDefault().postSticky(it)
            }
            cartItemLiveData.value?.let {
                if (it.isEmpty()) {
                    emptyStateLiveData.postValue(EmptyState(true, R.string.cartEmptyState))
                }
            }
        }.ignoreElement()
    }

    fun increaseCartItemCount(cartItem: CartItem): Completable =
        cartRepository
            .changeCount(cartItem.cart_item_id, ++cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count += 1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()

    fun decreaseCartItemCount(cartItem: CartItem): Completable =
        cartRepository.changeCount(cartItem.cart_item_id, --cartItem.count)
            .doAfterSuccess {
                calculateAndPublishPurchaseDetail()
                val cartItemCount = EventBus.getDefault().getStickyEvent(CartItemCount::class.java)
                cartItemCount?.let {
                    it.count -= 1
                    EventBus.getDefault().postSticky(it)
                }
            }
            .ignoreElement()

    fun refresh() {
        getCartItems()
    }

    private fun calculateAndPublishPurchaseDetail() {
        cartItemLiveData.value?.let { cartItems ->
            purchaseDetailLiveData.value?.let { purchaseDetail ->
                var totalPrice = 0
                var payablePrice = 0
                cartItems.forEach {
                    totalPrice += it.product.price * it.count
                    payablePrice += (it.product.price - it.product.discount) * it.count

                }
                purchaseDetail.total_price = totalPrice
                purchaseDetail.payable_price = payablePrice
                purchaseDetailLiveData.postValue(purchaseDetail)


            }

        }
    }


}