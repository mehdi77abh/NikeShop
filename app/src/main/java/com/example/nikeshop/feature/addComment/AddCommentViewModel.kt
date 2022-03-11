package com.example.nikeshop.feature.addComment

import android.os.Bundle
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.data.DataClass.MessageResponse
import com.example.nikeshop.data.repo.comment.CommentRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.Completable
import timber.log.Timber

class AddCommentViewModel(val productId: Int, val repository: CommentRepository) : NikeViewModel() {

    fun addComment(title: String, content: String): Completable =
        repository.addComment(productId, title, content).ignoreElement()


}