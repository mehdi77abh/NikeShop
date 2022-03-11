package com.example.nikeshop.feature.product.comment

import androidx.lifecycle.MutableLiveData
import com.example.nikeshop.NikeViewModel
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.data.repo.comment.CommentRepository
import com.sevenlearn.nikestore.common.asyncNetworkRequest

class CommentListViewModel(productId:Int,commentRepository: CommentRepository) : NikeViewModel() {
    val commentListLiveData = MutableLiveData<List<Comment>>()

    init {
        progressBarLiveData.value = true
        commentRepository.getComments(productId).asyncNetworkRequest()
            .doFinally { progressBarLiveData.value=false }
            .subscribe(object :NikeSingleObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    commentListLiveData.value = t
                }
            })
    }
}