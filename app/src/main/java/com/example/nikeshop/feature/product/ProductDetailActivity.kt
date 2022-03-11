package com.example.nikeshop.feature.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_ID
import com.example.nikeshop.common.NikeCompletableObserver
import com.example.nikeshop.common.NikeException
import com.example.nikeshop.common.NikeExceptionMapper
import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.feature.product.comment.CommentAdapter
import com.example.nikeshop.feature.product.comment.CommentListActivity
import com.example.nikeshop.serivce.ImageLoadingService
import com.example.nikeshop.customView.scroll.ObservableScrollViewCallbacks
import com.example.nikeshop.customView.scroll.ScrollState
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.data.DataClass.TokenContainer
import com.example.nikeshop.feature.Auth.AuthActivity
import com.example.nikeshop.feature.addComment.AddCommentActivity
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import com.sevenlearn.nikestore.common.formatPrice
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class ProductDetailActivity : NikeActivity() {
    val viewModel: ProductDetailViewModel by viewModel { parametersOf(intent.extras) }
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    val commentAdapter = CommentAdapter(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        viewModel.productLiveData.observe(this) {
            imageLoadingService.load(productIv, it.image)
            titleTv.text = it.title
            previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            previousPriceTv.text = formatPrice(it.previous_price.toString())
            currentPriceTv.text = formatPrice(it.price.toString())
            toolbarTitleTv.text = it.title
            changeFavoriteIcon(it)
            detailFavoriteBtn.setOnClickListener { view ->
                viewModel.addOrDeleteProductFromFavorites(it)
                it.isFavorite = !it.isFavorite
                changeFavoriteIcon(it)


            }


        }
        viewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }

        commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        commentsRv.adapter = commentAdapter
        viewModel.commentLiveData.observe(this) {
            commentAdapter.commentList = it as ArrayList<Comment>
            if (it.size > 3) {
                viewAllCommentsBtn.visibility = View.VISIBLE
                viewAllCommentsBtn.setOnClickListener {
                    startActivity(Intent(this, CommentListActivity::class.java).apply {
                        putExtra(EXTRA_KEY_ID, viewModel.productLiveData.value!!.id)
                    })
                }
            }
        }



        initialViews()


    }


    private fun initialViews() {

        addToCartBtn.setOnClickListener {
            viewModel.addToCartBtn().asyncNetworkRequest()
                .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                    override fun onComplete() {
                        showSnackBar(getString(R.string.successfulAddToCart))
                    }
                })
        }

        addCommentBtn.setOnClickListener {
            if (!TokenContainer.token.isNullOrEmpty()) {
                startActivity(Intent(this, AddCommentActivity::class.java)
                    .apply {
                        putExtra(EXTRA_KEY_ID, viewModel.productLiveData.value!!.id)
                    })
            } else {//TODO ADD NIKE ERROR EXCEPTION
                startActivity(Intent(this, AuthActivity::class.java))
                showToast(getString(R.string.loginForAddComment))
                //EventBus.getDefault().post(NikeExceptionMapper.map(NikeException(NikeException.Type.AUTH,R.string.loginForAddComment)))
            }
        }

        productIv.post {
            val productIvHeight = productIv.height
            val toolbar = toolbarView
            val productImageView = productIv
            observableScrollView.addScrollViewCallbacks(object : ObservableScrollViewCallbacks {
                override fun onScrollChanged(
                    scrollY: Int,
                    firstScroll: Boolean,
                    dragging: Boolean
                ) {

                    Timber.i("Height: $productIvHeight")
                    toolbar.alpha = scrollY.toFloat() / productIvHeight.toFloat()
                    productImageView.translationY = scrollY.toFloat() / 2
                }

                override fun onDownMotionEvent() {
                }

                override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
                }

            })
        }

        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun changeFavoriteIcon(product: Product) {
        if (product.isFavorite)
            detailFavoriteBtn.setImageResource(R.drawable.ic_favorite_fill)
        else
            detailFavoriteBtn.setImageResource(R.drawable.ic_favorites)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}