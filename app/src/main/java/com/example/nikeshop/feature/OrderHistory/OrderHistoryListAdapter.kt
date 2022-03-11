package com.example.nikeshop.feature.OrderHistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.R
import com.example.nikeshop.customView.NikeImageView
import com.sevenlearn.nikestore.common.convertDpToPixel
import com.sevenlearn.nikestore.common.formatPrice
import com.example.nikeshop.data.DataClass.OrderHistoryItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_order_history.view.*

class OrderHistoryListAdapter(val context: Context,val orderHistoryItems: List<OrderHistoryItem>) :
    RecyclerView.Adapter<OrderHistoryListAdapter.OrderViewHolder>() {
    val layoutParams: LinearLayout.LayoutParams

    init {

        val pixel = convertDpToPixel(100f, context).toInt()
        val margin = convertDpToPixel(8f, context).toInt()
        layoutParams = LinearLayout.LayoutParams(pixel, pixel)
        layoutParams.setMargins(margin, 0, margin, 0)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_history, parent, false)
        )

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) = holder.bind(orderHistoryItems[position])
    override fun getItemCount(): Int = orderHistoryItems.size


    inner class OrderViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(orderHistoryItem: OrderHistoryItem) {
            containerView.orderPriceTv.text = formatPrice(orderHistoryItem.payable.toString())
            containerView.orderIdTv.text = orderHistoryItem.id.toString()
            containerView.orderProductLl.removeAllViews()
            orderHistoryItem.order_items.forEach {
                val imageView = NikeImageView(context)
                imageView.layoutParams = layoutParams
                imageView.setImageURI(it.product.image)
                containerView.orderProductLl.addView(imageView)

            }

        }

    }

}