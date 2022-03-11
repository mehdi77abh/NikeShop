package com.example.nikeshop.data.repo.comment

import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.data.DataClass.MessageResponse
import io.reactivex.Single

class CommentRepositoryImpl(val remoteDataSource: CommentDataSource) : CommentRepository {
    override fun getComments(productId: Int): Single<List<Comment>> =
        remoteDataSource.getComments(productId)


    override fun addComment(
        productId: Int,
        title: String,
        content: String
    ): Single<Comment> =remoteDataSource.addComment(productId, title, content)

}