package com.example.nikeshop.feature.Favorites

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.feature.product.ProductDetailActivity
import kotlinx.android.synthetic.main.activity_favorite_list.*
import kotlinx.android.synthetic.main.view_default_empty_state.*
import kotlinx.android.synthetic.main.view_default_empty_state.view.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteListActivity : NikeActivity(),
    FavoriteProductAdapter.FavoriteProductEventListener {
    val viewModel: FavoriteListViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_list)

        viewModel.favoriteListLiveData.observe(this) {
            if (!it.isNullOrEmpty()) {
                helpBtn.visibility = View.VISIBLE
                favoriteProductsRv.layoutManager =
                    LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                favoriteProductsRv.adapter =
                    FavoriteProductAdapter(it as MutableList<Product>, get(), this)

            }else
                helpBtn.visibility = View.GONE
        }
        viewModel.emptyStateLiveData.observe(this) {
            if (it.mustShow) {
                val emptyState = showEmptyState(R.layout.view_default_empty_state)
                emptyState?.let {view->
                    view.emptyStateMessageTv.text = getString(it.messageResId)


                }

            }else
                emptyStateRootView?.visibility = View.INVISIBLE
        }

        favoriteToolbar.onBackBtnClickListener = View.OnClickListener {
            finish()
        }
        helpBtn.setOnClickListener {
            showToast(getString(R.string.favorite_help_message))
        }

    }

    override fun onClick(product: Product) {
        startActivity(Intent(this, ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onLongClick(product: Product) {
        viewModel.deleteFromFavorites(product)
        showToast(getString(R.string.productDeleted))
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllFavoritesProducts()
    }
}