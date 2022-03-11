package com.example.nikeshop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nikeshop.common.NikeException
import com.example.nikeshop.feature.Auth.AuthActivity
import com.example.nikeshop.feature.product.ProductDetailActivity
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.lang.IllegalStateException

abstract class NikeFragment : Fragment(), NikeView {
    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout

    override val viewContext: Context?
        get() = context

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)

    }
}

abstract class NikeActivity : AppCompatActivity(), NikeView {
    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.children.forEach {
                    if (it is CoordinatorLayout)
                        return it

                }
                throw IllegalStateException("Root View Must Be instance of coordinatorLayout")
            } else
                return viewGroup
        }

    override val viewContext: Context?
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}

interface NikeView {
    val rootView: CoordinatorLayout?
    val viewContext: Context?
    fun setProgressIndicator(mustShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->

                var loadingView = it.findViewById<View>(R.id.loadingView)
                if (loadingView == null && mustShow) {

                    loadingView =
                        LayoutInflater.from(context).inflate(
                            R.layout.loading_view, it, false
                        )
                    it.addView(loadingView)
                }
                loadingView?.visibility = if (mustShow) View.VISIBLE else View.GONE
            }
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showError(nikeException: NikeException) {
        viewContext?.let {
            when (nikeException.type) {
                NikeException.Type.SIMPLE -> {
                            val snackbar = showSnackBar(nikeException.serverMessage?:it.getString(nikeException.userFriendlyMessage))

                }
                NikeException.Type.AUTH -> {
                    if (it is ProductDetailActivity){
                        Timber.i(it.packageName)
                        it.startActivity(Intent(it, AuthActivity::class.java))
                        Toast.makeText(it, nikeException.serverMessage, Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }
    }

    fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT):Snackbar? {
        rootView?.let {
            val snackbar =Snackbar.make(it, message, duration)
                snackbar.show()
            return snackbar
        }
        return null
    }
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT){
        viewContext?.let {
            Toast.makeText(it,message,duration).show()
        }
    }

    fun showEmptyState(layoutResId: Int): View? {
        rootView?.let {
            viewContext?.let { context ->
                var emptyState = it.findViewById<View>(layoutResId)
                if (emptyState == null) {
                    emptyState = LayoutInflater.from(context)
                        .inflate(layoutResId, rootView, false)
                    it.addView(emptyState)
                }
                emptyState.visibility = View.VISIBLE
                return emptyState
            }
        }
        return null
    }


}

abstract class NikeViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val progressBarLiveData = MutableLiveData<Boolean>()
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

