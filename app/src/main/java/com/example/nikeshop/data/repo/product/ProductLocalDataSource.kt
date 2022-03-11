package com.example.nikeshop.data.repo.product

import androidx.room.*
import com.example.nikeshop.data.DataClass.Product
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ProductLocalDataSource: ProductDataSource {

    override fun getProducts(sort:Int): Single<List<Product>> {
        TODO("Not yet implemented")
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun addToFavorite(product: Product): Completable
    @Query("SELECT * FROM products")
    override fun getFavoriteProducts(): Single<List<Product>>
    @Delete
    override fun deleteFromFavorites(product: Product): Completable

    override fun search(query: String): Single<List<Product>> {
        TODO("Not yet implemented")
    }
}