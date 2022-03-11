package com.example.nikeshop.data.DataClass

import androidx.annotation.StringRes

data class EmptyState (
    val mustShow:Boolean,@StringRes val messageResId:Int=0,val mustShowCallToAction:Boolean=false
        )