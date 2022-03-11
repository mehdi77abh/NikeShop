package com.example.nikeshop.feature.product.list

import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.R
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.data.repo.product.ProductRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest

class ProductListViewModel(var sort: Int, val productRepository: ProductRepository) :
    NikeViewModel() {
    val productListLiveData = MutableLiveData<List<Product>>()
    var selectedSortLiveData = MutableLiveData<Int>()
    val sortTitle = arrayOf(
        R.string.sortLatest,
        R.string.sortPopular,
        R.string.sortPriceHighToLow,
        R.string.sortPriceLowToHigh
    )

    init {
        getProductListBySort()
        selectedSortLiveData.value = sortTitle[sort]
    }

    fun getProductListBySort() {
        progressBarLiveData.value = true
        productRepository.getProducts(sort).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value = false }
            .subscribe(object : NikeSingleObserver<List<Product>>(compositeDisposable) {
                override fun onSuccess(t: List<Product>) {
                    productListLiveData.value = t
                }
            })


    }

    fun onSelectedSortChangeByUser(sort: Int) {
        this.sort = sort
        this.selectedSortLiveData.value = sortTitle[sort]
        getProductListBySort()
    }

}