package com.example.nikeshop.common

import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

abstract class NikeSingleObserver<T>(val compositeDisposable: CompositeDisposable) :
    SingleObserver<T> {
    override fun onSubscribe(d: Disposable) {
        compositeDisposable.add(d)
    }


    override fun onError(e: Throwable) {
        Timber.e("Nike Error is :${e.message}")
        EventBus.getDefault().post(NikeExceptionMapper.map(e))

    }

}