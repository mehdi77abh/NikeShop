package com.example.nikeshop.serivce.http

import com.example.nikeshop.data.DataClass.*
import com.google.gson.JsonObject
import com.sevenlearn.nikestore.data.Checkout
import com.sevenlearn.nikestore.data.SubmitOrderResult
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("product/list")
    fun getProducts(@Query("sort") sort: String): Single<List<Product>>

    @GET("banner/slider")
    fun getBanners(): Single<List<Banner>>

    @GET("comment/list")
    fun getComments(@Query("product_id") productId: Int): Single<List<Comment>>

    @POST("cart/add")
    fun addToCart(@Body jsonObject: JsonObject): Single<AddToCartResponse>

    @POST("cart/remove")
    fun removeItemFromCart(@Body jsonObject: JsonObject):Single<MessageResponse>

    @GET("cart/list")
    fun getCart():Single<CartResponse>

    @POST("cart/changeCount")
    fun changeCount(@Body jsonObject: JsonObject):Single<AddToCartResponse>

    @GET("cart/count")
    fun getCartItemCount():Single<CartItemCount>

    @POST("auth/token")
    fun login(@Body jsonObject: JsonObject): Single<TokenResponse>

    @POST("user/register")
    fun signUp(@Body jsonObject: JsonObject): Single<MessageResponse>

    @POST("auth/token")
    fun refreshToken(@Body jsonObject: JsonObject): Call<TokenResponse>

    @POST("order/submit")
    fun submitOrder(@Body jsonObject: JsonObject):Single<SubmitOrderResult>

    @GET("order/checkout")
    fun checkout(@Query("order_id") orderId:Int):Single<Checkout>

    @POST("comment/add")
    fun addComment(@Body jsonObject: JsonObject):Single<Comment>

    @GET("product/search")
    fun search(@Query("q") query:String):Single<List<Product>>

    @GET("order/list")
    fun getOrderHistory():Single<List<OrderHistoryItem>>
}

fun createApiServiceInstance(): ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = it.request().newBuilder()
            if (TokenContainer.token != null)
                newRequestBuilder.addHeader("Authorization", "Bearer ${TokenContainer.token}")


            newRequestBuilder.addHeader("Accept", "application/json")
            newRequestBuilder.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequestBuilder.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()


    val retrofit = Retrofit.Builder()
        .baseUrl("http://expertdevelopers.ir/api/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)

}