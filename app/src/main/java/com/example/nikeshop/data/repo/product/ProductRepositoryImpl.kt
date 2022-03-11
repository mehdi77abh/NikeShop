package com.example.nikeshop.data.repo.product

import com.example.nikeshop.data.DataClass.Product
import io.reactivex.Completable
import io.reactivex.Single

class ProductRepositoryImpl(
    val remoteDataSource: ProductDataSource,
    val localDataSource: ProductDataSource
) : ProductRepository {

    override fun getProducts(sort: Int): Single<List<Product>> =
        localDataSource.getFavoriteProducts().flatMap { favoriteProducts ->
            remoteDataSource.getProducts(sort).doOnSuccess {
                val favoritesProductIds=favoriteProducts.map {
                    it.id
                }
                it.forEach { product->
                    if (favoritesProductIds.contains(product.id)){
                        product.isFavorite = true
                    }
                }
            }
        }

    override fun addToFavorite(product: Product): Completable =
        localDataSource.addToFavorite(product)

    override fun getFavoriteProducts(): Single<List<Product>> =
        localDataSource.getFavoriteProducts()

    override fun deleteFromFavorites(product: Product): Completable =
        localDataSource.deleteFromFavorites(product)

    override fun search(query: String): Single<List<Product>> = remoteDataSource.search(query)
}