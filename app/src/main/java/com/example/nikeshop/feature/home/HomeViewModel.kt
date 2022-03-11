package com.example.nikeshop.feature.home

import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.NikeCompletableObserver
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.*
import com.example.nikeshop.data.repo.banner.BannerRepository
import com.example.nikeshop.data.repo.product.ProductRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(private val repository: ProductRepository, bannerRepository: BannerRepository) :
    NikeViewModel() {

    val latestProductsListLiveData = MutableLiveData<List<Product>>()
    val popularProductsListLiveData = MutableLiveData<List<Product>>()
    val lowerPriceProductsListLiveData = MutableLiveData<List<Product>>()
    val higherPriceProductsListLiveData = MutableLiveData<List<Product>>()
    val searchProductsListLiveData = MutableLiveData<List<Product>>()
    var viewPagerHeight: Float = 0F

    val bannerLiveData = MutableLiveData<List<Banner>>()

    init {
        getAllProducts()
        bannerRepository.getBanners().asyncNetworkRequest()
            .subscribe(object : NikeSingleObserver<List<Banner>>(compositeDisposable) {
                override fun onSuccess(t: List<Banner>) {
                    bannerLiveData.value = t
                }
            })
    }

    fun addOrDeleteProductFromFavorites(product: Product) {
        if (product.isFavorite) {
            repository.deleteFromFavorites(product).asyncNetworkRequest()
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                       product.isFavorite= false
                    }
                })
        }else{
                repository.addToFavorite(product).asyncNetworkRequest()
                    .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                        override fun onComplete() {
                            product.isFavorite= true
                        }
                    })
            }


    }

    fun search(query:String){


        progressBarLiveData.value =true
        repository.search(query).asyncNetworkRequest()
            .doOnSuccess { progressBarLiveData.value = false }
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    searchProductsListLiveData.value = t
                }
            })
    }

    fun getProductBySort(sort:Int,productLiveData:MutableLiveData<List<Product>>){
        progressBarLiveData.value = true
        repository.getProducts(sort).asyncNetworkRequest().doFinally { progressBarLiveData.value =false }
            .subscribe(object :NikeSingleObserver<List<Product>>(compositeDisposable){
                override fun onSuccess(t: List<Product>) {
                    productLiveData.value =t
                }
            })

    }

    fun getAllProducts(){

        getProductBySort(SORT_LATEST,latestProductsListLiveData)
        getProductBySort(SORT_POPULAR,popularProductsListLiveData)
        getProductBySort(SORT_PRICE_ASC,higherPriceProductsListLiveData)
        getProductBySort(SORT_PRICE_DESC,lowerPriceProductsListLiveData)

    }
}