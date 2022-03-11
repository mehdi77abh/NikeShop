package com.example.nikeshop.serivce

import com.example.nikeshop.customView.NikeImageView
import com.facebook.drawee.view.SimpleDraweeView

class FrescoImageLoadingServiceImpl : ImageLoadingService {

    override fun load(imageView: NikeImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView)
            imageView.setImageURI(imageUrl)
        else
            throw IllegalStateException("ImageView Must instance of SimpleDraweeView")
    }

}