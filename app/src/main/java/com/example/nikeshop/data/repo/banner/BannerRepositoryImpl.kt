package com.example.nikeshop.data.repo.banner

import com.example.nikeshop.data.DataClass.Banner
import io.reactivex.Single

class BannerRepositoryImpl(val bannerDataSource: BannerDataSource): BannerRepository {

    override fun getBanners(): Single<List<Banner>> = bannerDataSource.getBanners()
}