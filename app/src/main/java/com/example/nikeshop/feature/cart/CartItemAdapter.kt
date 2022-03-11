package com.example.nikeshop.feature.cart

import android.graphics.Paint
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ContentInfoCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.R
import com.example.nikeshop.common.CartItemException
import com.example.nikeshop.common.FaNum
import com.example.nikeshop.common.NikeException
import com.example.nikeshop.common.NikeExceptionMapper
import com.example.nikeshop.data.DataClass.CartItem
import com.example.nikeshop.data.DataClass.PurchaseDetail
import com.example.nikeshop.serivce.ImageLoadingService
import com.sevenlearn.nikestore.common.formatPrice
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_cart.view.*
import kotlinx.android.synthetic.main.item_purchase_details.view.*
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import kotlin.coroutines.coroutineContext

const val VIEW_TYPE_CART_ITEM = 0
const val VIEW_TYPE_PURCHASE_DETAIL_ITEM = 1

class CartItemAdapter(
    val cartItems: MutableList<CartItem>,
    val imageLoadingService: ImageLoadingService,
    val cartItemViewCallBacks: CartItemViewCallBacks
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var purchaseDetail: PurchaseDetail? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_CART_ITEM)
            return CartViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
            )
        else
            return PurchaseDetailViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_purchase_details, parent, false)
            )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CartViewHolder) {
            holder.bindCartItem(cartItems[position])
        } else if (holder is PurchaseDetailViewHolder) {
            purchaseDetail?.let {
                holder.bind(it.total_price, it.shipping_cost, it.payable_price)
            }
        }

    }

    override fun getItemCount(): Int = cartItems.size + 1


    override fun getItemViewType(position: Int): Int =
        if (position == cartItems.size) VIEW_TYPE_PURCHASE_DETAIL_ITEM else VIEW_TYPE_CART_ITEM

    inner class CartViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindCartItem(cartItem: CartItem) {
            imageLoadingService.load(containerView.productIv, cartItem.product.image)
            containerView.productTitleTv.text = cartItem.product.title
            containerView.priceTv.text =
                formatPrice((cartItem.product.price - cartItem.product.discount).toString())
            containerView.previousPriceTv.text =
                formatPrice((cartItem.product.price).toString())
            containerView.previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            containerView.cartItemCountTv.text = FaNum.convert(cartItem.count.toString())


            containerView.changeCountProgressBar.visibility =
                if (cartItem.changeCountProgressBarVisible) View.VISIBLE else View.INVISIBLE

            containerView.cartItemCountTv.visibility =
                if (cartItem.changeCountProgressBarVisible) View.INVISIBLE else View.VISIBLE


            containerView.removeFromCartBtn.setOnClickListener {
                cartItemViewCallBacks.onRemoveBtnClicked(cartItem)
            }
            containerView.decreaseBtn.setOnClickListener {
                if (cartItem.count > 1) {
                    cartItem.changeCountProgressBarVisible = true
                    containerView.changeCountProgressBar.visibility = View.VISIBLE
                    containerView.cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallBacks.onDecreaseCartItemBtnClicked(cartItem)

                } else if (cartItem.count == 1) {
                    EventBus.getDefault().post(
                        NikeExceptionMapper.map(
                            CartItemException(
                                NikeException.Type.SIMPLE, R.string.cartLowCountError
                            )
                        )
                    )

                }
            }
            containerView.increaseBtn.setOnClickListener {
                if (cartItem.count < 5) {
                    cartItem.changeCountProgressBarVisible = true
                    containerView.changeCountProgressBar.visibility = View.VISIBLE
                    containerView.cartItemCountTv.visibility = View.INVISIBLE
                    cartItemViewCallBacks.onIncreaseCartItemBtnClicked(cartItem)

                } else
                    EventBus.getDefault().post(
                        NikeExceptionMapper
                            .map(CartItemException(
                                    NikeException.Type.SIMPLE,
                                    R.string.cartMostCountError))
                    )


            }
            containerView.productIv.setOnClickListener {
                cartItemViewCallBacks.onImageProductClicked(cartItem)
            }
        }

    }

    class PurchaseDetailViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(totalPrice: Int, shippingPrice: Int, payablePrice: Int) {
            containerView.totalPriceTv.text = formatPrice(totalPrice.toString())
            containerView.shippingCostTv.text = formatPrice(shippingPrice.toString())
            containerView.payablePriceTv.text = formatPrice(payablePrice.toString())
        }

    }

    fun removeCartItem(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun increaseCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarVisible = false
            notifyItemChanged(index)
        }
    }

    fun decreaseCount(cartItem: CartItem) {
        val index = cartItems.indexOf(cartItem)
        if (index > -1) {
            cartItems[index].changeCountProgressBarVisible = false
            notifyItemChanged(index)
        }
    }

    interface CartItemViewCallBacks {
        fun onRemoveBtnClicked(cartItem: CartItem)

        fun onIncreaseCartItemBtnClicked(cartItem: CartItem)

        fun onDecreaseCartItemBtnClicked(cartItem: CartItem)

        fun onImageProductClicked(cartItem: CartItem)


    }
}