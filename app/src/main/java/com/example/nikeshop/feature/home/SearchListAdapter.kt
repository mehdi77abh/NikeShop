package com.example.nikeshop.feature.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.R
import com.example.nikeshop.customView.NikeImageView
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.serivce.ImageLoadingService

class SearchListAdapter(

    private val imageLoadingService: ImageLoadingService
) : RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {

    var eventListener: SearchEventListener? = null

    var productList = ArrayList<Product>()
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_product, parent, false)
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bindView(productList[position])

    override fun getItemCount(): Int = productList.size

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.productTitleTv)
        val productImg = itemView.findViewById<NikeImageView>(R.id.productIv)
        fun bindView(product: Product) {
            titleTv.text = product.title
            imageLoadingService.load(productImg, product.image)
            itemView.setOnClickListener {
                eventListener!!.onSearchedProductClick(product)
            }
        }
    }

    interface SearchEventListener {
        fun onSearchedProductClick(product: Product)
    }
}