package com.example.nikeshop.data.repo.comment

import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.data.DataClass.MessageResponse
import io.reactivex.Single

interface CommentDataSource {

    fun getComments(productId: Int): Single<List<Comment>>
    fun addComment(productId: Int,title:String,content:String):Single<Comment>

}