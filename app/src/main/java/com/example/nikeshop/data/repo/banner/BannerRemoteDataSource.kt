package com.example.nikeshop.data.repo.banner

import com.example.nikeshop.data.DataClass.Banner
import com.example.nikeshop.serivce.http.ApiService
import io.reactivex.Single

class BannerRemoteDataSource(val apiService: ApiService): BannerDataSource {

    override fun getBanners(): Single<List<Banner>> =apiService.getBanners()
}