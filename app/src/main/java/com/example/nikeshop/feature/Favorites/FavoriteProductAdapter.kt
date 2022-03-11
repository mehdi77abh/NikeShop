package com.example.nikeshop.feature.Favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.R
import com.example.nikeshop.customView.NikeImageView
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.serivce.ImageLoadingService

class FavoriteProductAdapter(
    val productList: MutableList<Product>,
    val imageLoadingService: ImageLoadingService,
    val favoriteProductEventListener: FavoriteProductEventListener
) :
    RecyclerView.Adapter<FavoriteProductAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite_product, parent, false)
        )


    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.productTitleTv)
        val imageView = itemView.findViewById<NikeImageView>(R.id.productIv)
        fun bind(product: Product) {
            titleTv.text = product.title
            imageLoadingService.load(imageView, product.image)
            itemView.setOnClickListener { favoriteProductEventListener.onClick(product) }
            itemView.setOnLongClickListener {
                productList.remove(product)
                notifyItemRemoved(adapterPosition)
                favoriteProductEventListener.onLongClick(product)
                return@setOnLongClickListener false

            }
        }
    }

    interface FavoriteProductEventListener {
        fun onClick(product: Product)
        fun onLongClick(product: Product)
    }
}