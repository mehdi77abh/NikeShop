package com.example.nikeshop.data.repo.product

import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.serivce.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single

class ProductRemoteDataSource(val apiService: ApiService): ProductDataSource {

    override fun getProducts(sort:Int): Single<List<Product>> =apiService.getProducts(sort.toString())

    override fun addToFavorite(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun getFavoriteProducts(): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(product: Product): Completable {
        TODO("Not yet implemented")
    }

    override fun search(query: String): Single<List<Product>> =apiService.search(query)
}