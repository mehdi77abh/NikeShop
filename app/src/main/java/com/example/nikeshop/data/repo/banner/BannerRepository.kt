package com.example.nikeshop.data.repo.banner

import com.example.nikeshop.data.DataClass.Banner
import io.reactivex.Single

interface BannerRepository {

    fun getBanners(): Single<List<Banner>>
}