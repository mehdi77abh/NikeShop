package com.example.nikeshop.feature.common

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.R
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.serivce.ImageLoadingService
import com.example.nikeshop.customView.NikeImageView
import com.sevenlearn.nikestore.common.formatPrice
import com.sevenlearn.nikestore.common.implementSpringAnimationTrait
import java.lang.IllegalStateException

const val VIEW_TYPE_ROUND = 0
const val VIEW_TYPE_SMALL = 1
const val VIEW_TYPE_LARGE = 2

class ProductListAdapter(
    var viewType: Int = VIEW_TYPE_ROUND,
    val imageLoadingService: ImageLoadingService
) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {
    var productEventListener: ProductEventListener? = null

    var productList = ArrayList<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutResId = when (viewType) {
            VIEW_TYPE_ROUND -> R.layout.item_product
            VIEW_TYPE_SMALL -> R.layout.item_product_small
            VIEW_TYPE_LARGE -> R.layout.item_product_large
            else -> throw IllegalStateException("View Type Is Not Value")
        }

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindProduct(productList[position])

    override fun getItemCount(): Int = productList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.productTitleTv)
        val previousPriceTv = itemView.findViewById<TextView>(R.id.previousPriceTv)
        val currentPriceTv = itemView.findViewById<TextView>(R.id.currentPriceTv)
        val productIv = itemView.findViewById<NikeImageView>(R.id.productIv)
        val favIv = itemView.findViewById<ImageView>(R.id.favoriteBtn)

        fun bindProduct(product: Product) {
            imageLoadingService.load(productIv, product.image)
            titleTv.text = product.title

            previousPriceTv.text = formatPrice(product.previous_price.toString())
            currentPriceTv.text = formatPrice(product.price.toString())
            previousPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                productEventListener?.OnProductClick(product)
            }
            if (product.isFavorite)
                favIv.setImageResource(R.drawable.ic_favorite_fill)
            else
                favIv.setImageResource(R.drawable.ic_favorites)

            favIv.setOnClickListener {
                productEventListener?.OnFavoriteBtnClick(product)
                product.isFavorite = !product.isFavorite

                notifyItemChanged(adapterPosition)
            }
        }

    }

    interface ProductEventListener {
        fun OnProductClick(product: Product)
        fun OnFavoriteBtnClick(product: Product)

    }
}