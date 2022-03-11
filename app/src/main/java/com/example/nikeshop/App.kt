package com.example.nikeshop

import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import androidx.room.Room
import com.example.nikeshop.data.db.AppDataBase
import com.example.nikeshop.data.repo.banner.BannerRemoteDataSource
import com.example.nikeshop.data.repo.banner.BannerRepository
import com.example.nikeshop.data.repo.banner.BannerRepositoryImpl
import com.example.nikeshop.data.repo.cart.CartRemoteDataSource
import com.example.nikeshop.data.repo.cart.CartRepository
import com.example.nikeshop.data.repo.cart.CartRepositoryImpl
import com.example.nikeshop.data.repo.comment.CommentRemoteDataSource
import com.example.nikeshop.data.repo.comment.CommentRepository
import com.example.nikeshop.data.repo.comment.CommentRepositoryImpl
import com.example.nikeshop.data.repo.order.OrderRemoteDataSource
import com.example.nikeshop.data.repo.order.OrderRepository
import com.example.nikeshop.data.repo.order.OrderRepositoryImpl
import com.example.nikeshop.data.repo.product.ProductRemoteDataSource
import com.example.nikeshop.data.repo.product.ProductRepository
import com.example.nikeshop.data.repo.product.ProductRepositoryImpl
import com.example.nikeshop.data.repo.user.UserLocalDataSource
import com.example.nikeshop.data.repo.user.UserRemoteDataSource
import com.example.nikeshop.data.repo.user.UserRepository
import com.example.nikeshop.data.repo.user.UserRepositoryImpl
import com.example.nikeshop.feature.Auth.AuthViewModel
import com.example.nikeshop.feature.Favorites.FavoriteListViewModel
import com.example.nikeshop.feature.OrderHistory.OrderHistoryViewModel
import com.example.nikeshop.feature.addComment.AddCommentViewModel
import com.example.nikeshop.feature.cart.CartViewModel
import com.example.nikeshop.feature.common.ProductListAdapter
import com.example.nikeshop.feature.home.HomeViewModel
import com.example.nikeshop.feature.home.SearchListAdapter
import com.example.nikeshop.feature.main.MainViewModel
import com.example.nikeshop.feature.product.ProductDetailViewModel
import com.example.nikeshop.feature.product.comment.CommentListViewModel
import com.example.nikeshop.feature.product.list.ProductListViewModel
import com.example.nikeshop.feature.profile.ProfileViewModel
import com.example.nikeshop.feature.shipping.CheckoutViewModel
import com.example.nikeshop.feature.shipping.ShippingViewModel
import com.example.nikeshop.serivce.FrescoImageLoadingServiceImpl
import com.example.nikeshop.serivce.ImageLoadingService
import com.example.nikeshop.serivce.http.createApiServiceInstance
import com.facebook.drawee.backends.pipeline.Fresco
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Fresco.initialize(this)
        val myModule = module {
            single { createApiServiceInstance() }
            single<ImageLoadingService> { FrescoImageLoadingServiceImpl() }
            single { Room.databaseBuilder(this@App,AppDataBase::class.java,"db_app").build() }
            factory<ProductRepository> {
                ProductRepositoryImpl(
                    ProductRemoteDataSource(get()),
                    get<AppDataBase>().productDao()
                )

            }
            single<SharedPreferences> {
                this@App.getSharedPreferences(
                    "app_settings",
                    MODE_PRIVATE
                )
            }
            single { UserLocalDataSource(get()) }
            single<UserRepository> { UserRepositoryImpl(
                UserLocalDataSource(get()),
                UserRemoteDataSource(get())
            ) }
            single<OrderRepository> { OrderRepositoryImpl(OrderRemoteDataSource(get())) }
            factory { (viewType: Int) -> ProductListAdapter(viewType, get()) }
            factory<BannerRepository> { BannerRepositoryImpl(BannerRemoteDataSource(get())) }
            factory<CommentRepository> { CommentRepositoryImpl(CommentRemoteDataSource(get())) }
            factory<CartRepository> { CartRepositoryImpl(CartRemoteDataSource(get())) }

            factory { SearchListAdapter(get()) }

            viewModel { HomeViewModel(get(), get()) }
            viewModel { (bundle: Bundle) -> ProductDetailViewModel(get(), get(),get(), bundle) }
            viewModel { (productId: Int) -> CommentListViewModel(productId, get()) }
            viewModel { (sort: Int) -> ProductListViewModel(sort, get()) }
            viewModel { AuthViewModel(get()) }
            viewModel { CartViewModel(get()) }
            viewModel { MainViewModel(get()) }
            viewModel { ShippingViewModel(get()) }
            viewModel {(orderId:Int)->CheckoutViewModel(get(),orderId)  }
            viewModel { FavoriteListViewModel(get()) }
            viewModel { ProfileViewModel(get()) }
            viewModel {(productId :Int)-> AddCommentViewModel(productId,get()) }
            viewModel { OrderHistoryViewModel(get()) }
        }

        startKoin {
            androidContext(this@App)
            modules(myModule)
        }

        val userRepository: UserRepository = get()
        userRepository.loadToken()
    }
}