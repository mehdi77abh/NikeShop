package com.example.nikeshop.data.repo.product

import com.example.nikeshop.data.DataClass.Product
import io.reactivex.Completable
import io.reactivex.Single

interface ProductRepository {
    fun getProducts(sort:Int):Single<List<Product>>

    fun addToFavorite(product: Product):Completable

    fun getFavoriteProducts():Single<List<Product>>

    fun deleteFromFavorites(product: Product):Completable

    fun search(query:String):Single<List<Product>>

}