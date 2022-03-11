package com.example.nikeshop.data.repo.banner

import com.example.nikeshop.data.DataClass.Banner
import io.reactivex.Single

interface BannerDataSource {
    fun getBanners(): Single<List<Banner>>

}