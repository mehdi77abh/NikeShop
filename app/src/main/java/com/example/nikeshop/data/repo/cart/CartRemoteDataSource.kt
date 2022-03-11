package com.example.nikeshop.data.repo.cart

import com.example.nikeshop.data.DataClass.AddToCartResponse
import com.example.nikeshop.data.DataClass.CartItemCount
import com.example.nikeshop.data.DataClass.CartResponse
import com.example.nikeshop.data.DataClass.MessageResponse
import com.example.nikeshop.serivce.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CartRemoteDataSource(val apiService: ApiService) : CartDataSource {
    override fun addToCart(productId: Int): Single<AddToCartResponse> = apiService.addToCart(
        JsonObject().apply {
            addProperty("product_id", productId)
        }
    )

    override fun get(): Single<CartResponse> = apiService.getCart()

    override fun remove(cartItemId: Int): Single<MessageResponse> =
        apiService.removeItemFromCart(JsonObject().apply {
            addProperty("cart_item_id", cartItemId)
        })

    override fun changeCount(cartItemId: Int, count: Int): Single<AddToCartResponse> =
        apiService.changeCount(JsonObject().apply {
            addProperty("count", count)
            addProperty("cart_item_id", cartItemId)
        })

    override fun getCartItemsCount(): Single<CartItemCount> =apiService.getCartItemCount()
}