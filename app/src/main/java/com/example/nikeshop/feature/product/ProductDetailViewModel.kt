package com.example.nikeshop.feature.product

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.common.NikeCompletableObserver
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.data.repo.cart.CartRepository
import com.example.nikeshop.data.repo.comment.CommentRepository
import com.example.nikeshop.data.repo.product.ProductRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.Completable

class ProductDetailViewModel(
    val cartRepository: CartRepository,
    commentRepository: CommentRepository,
    val productRepository: ProductRepository,
    bundle: Bundle
) : NikeViewModel() {
    val productLiveData = MutableLiveData<Product>()
    val commentLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        productLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
        commentRepository.getComments(productLiveData.value!!.id).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Comment>>(compositeDisposable) {
                override fun onSuccess(t: List<Comment>) {
                    commentLiveData.value = t
                }
            })

    }

    fun addToCartBtn(): Completable =
        cartRepository.addToCart(productLiveData.value!!.id).ignoreElement()

    fun addOrDeleteProductFromFavorites(product: Product) {
        if (product.isFavorite) {
            productRepository.deleteFromFavorites(product).asyncNetworkRequest()
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite= false
                    }
                })
        }else{
            productRepository.addToFavorite(product).asyncNetworkRequest()
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        product.isFavorite= true
                    }
                })
        }


    }

}