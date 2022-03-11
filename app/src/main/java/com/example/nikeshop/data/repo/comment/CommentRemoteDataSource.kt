package com.example.nikeshop.data.repo.comment

import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.data.DataClass.MessageResponse
import com.example.nikeshop.serivce.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CommentRemoteDataSource(val apiService :ApiService): CommentDataSource {
    override fun getComments(productId: Int): Single<List<Comment>> =apiService.getComments(productId)

    override fun addComment(
        productId: Int,
        title: String,
        content: String
    ): Single<Comment> = apiService.addComment(JsonObject().apply {
        addProperty("title",title)
        addProperty("content",content)
        addProperty("product_id",productId)
    })
}