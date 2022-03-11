package com.example.nikeshop.feature.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.NikeFragment
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.common.NikeCompletableObserver
import com.example.nikeshop.common.NikeException
import com.example.nikeshop.data.DataClass.CartItem
import com.example.nikeshop.feature.Auth.AuthActivity
import com.example.nikeshop.feature.product.ProductDetailActivity
import com.example.nikeshop.feature.shipping.ShippingActivity
import com.example.nikeshop.serivce.ImageLoadingService
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_purchase_details.*
import kotlinx.android.synthetic.main.view_empty_state_cart.*
import kotlinx.android.synthetic.main.view_empty_state_cart.view.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class CartFragment : NikeFragment(), CartItemAdapter.CartItemViewCallBacks {

    val viewModel: CartViewModel by viewModel()
    var cartItemAdapter: CartItemAdapter? = null
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }


        viewModel.cartItemLiveData.observe(viewLifecycleOwner) {
            cartRv.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            cartItemAdapter =
                CartItemAdapter(it as MutableList<CartItem>, imageLoadingService, this)
            cartRv.adapter = cartItemAdapter

        }


        viewModel.purchaseDetailLiveData.observe(viewLifecycleOwner) {
            cartItemAdapter?.let { adapter ->
                adapter.purchaseDetail = it
                adapter.notifyItemChanged(adapter.cartItems.size)

            }
        }
        viewModel.emptyStateLiveData.observe(viewLifecycleOwner) {
            if (it.mustShow) {
                val emptyState = showEmptyState(R.layout.view_empty_state_cart)
                emptyState?.let { view ->
                    view.emptyStateMessage.text = getString(it.messageResId)
                    view.emptyStateCtBtn.visibility =
                        if (it.mustShowCallToAction) View.VISIBLE else View.INVISIBLE
                    view.emptyStateCtBtn.setOnClickListener {
                        startActivity(Intent(requireContext(), AuthActivity::class.java))
                    }
                }
            }else
                emptyStateRootView?.visibility = View.GONE
        }



        payBtn.setOnClickListener {
            startActivity(Intent(requireContext(),ShippingActivity::class.java).apply {
                putExtra(EXTRA_KEY_DATA,viewModel.purchaseDetailLiveData.value)
            })
        }



    }

    override fun onStart() {
        super.onStart()
        viewModel.refresh()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onRemoveBtnClicked(cartItem: CartItem) {
        viewModel.removeItemFromCart(cartItem).asyncNetworkRequest()
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    cartItemAdapter?.removeCartItem(cartItem)
                    showSnackBar("ایتم از سبد خرید حذف شد")
                }
            })
    }

    override fun onIncreaseCartItemBtnClicked(cartItem: CartItem) {
        viewModel.increaseCartItemCount(cartItem).asyncNetworkRequest()
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onComplete() {
                    //showSnackBar("تعداد ایتم ها افزایش یافت")
                    cartItemAdapter?.increaseCount(cartItem)
                    Timber.i(cartItem.toString())
                }
            })
    }

    override fun onDecreaseCartItemBtnClicked(cartItem: CartItem) {
        viewModel.decreaseCartItemCount(cartItem).asyncNetworkRequest()
            .subscribe(object : NikeCompletableObserver(compositeDisposable) {
                override fun onError(e: Throwable) {

                }
                override fun onComplete() {
                    cartItemAdapter?.decreaseCount(cartItem)
                    Timber.i(cartItem.toString())

                }
            })
    }

    override fun onImageProductClicked(cartItem: CartItem) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, cartItem.product)
        })
    }


}