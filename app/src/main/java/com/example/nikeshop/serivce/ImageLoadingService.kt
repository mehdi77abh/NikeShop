package com.example.nikeshop.serivce

import com.example.nikeshop.customView.NikeImageView

interface ImageLoadingService {


    fun load(imageView:NikeImageView,imageUrl:String)
}