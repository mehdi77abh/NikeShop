package com.example.nikeshop.feature.Favorites

import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.R
import com.example.nikeshop.common.NikeCompletableObserver
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.EmptyState
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.data.repo.product.ProductRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import timber.log.Timber

class FavoriteListViewModel(private val productRepository: ProductRepository) : NikeViewModel() {
    val favoriteListLiveData = MutableLiveData<List<Product>>()
    val emptyStateLiveData = MutableLiveData<EmptyState>()


    fun getAllFavoritesProducts() {
        productRepository.getFavoriteProducts().asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    if (t.isNullOrEmpty())
                        emptyStateLiveData.value =
                            EmptyState(true, R.string.favorite_empty_state_message)
                    else {
                        emptyStateLiveData.value = EmptyState(false)
                        favoriteListLiveData.value = t

                    }
                }
            })
    }

    fun deleteFromFavorites(product: Product) {
        productRepository.deleteFromFavorites(product).asyncNetworkRequest()
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Product Deleted From Favorites")
                    if(favoriteListLiveData.value!!.isEmpty()){
                        emptyStateLiveData.value = EmptyState(true,R.string.favorite_empty_state_message)
                    }
                }
            })

    }

    fun addToFavorites(product: Product) {
        productRepository.addToFavorite(product).asyncNetworkRequest()
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    Timber.i("Product Added To Favorites")
                }
            })
    }


}