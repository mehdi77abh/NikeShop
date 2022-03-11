package com.example.nikeshop.feature.product.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.data.DataClass.Product
import com.example.nikeshop.feature.common.ProductListAdapter
import com.example.nikeshop.feature.common.VIEW_TYPE_LARGE
import com.example.nikeshop.feature.common.VIEW_TYPE_SMALL
import com.example.nikeshop.feature.product.ProductDetailActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.item_product_large.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductListActivity : NikeActivity(), ProductListAdapter.ProductEventListener {
    val viewModel: ProductListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_DATA
            )
        )
    }
    val adapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_SMALL) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        val gridLayoutManager = GridLayoutManager(this,2)
        productRv.layoutManager = gridLayoutManager
        productRv.adapter = adapter
        viewModel.productListLiveData.observe(this) {
        adapter.productList =it as ArrayList<Product>
        }
        toolbarView.onBackBtnClickListener = View.OnClickListener {
            finish()
        }
        adapter.productEventListener =this
        viewTypeChangerBtn.setOnClickListener {
            if (adapter.viewType == VIEW_TYPE_SMALL){
                adapter.viewType = VIEW_TYPE_LARGE
                viewTypeChangerBtn.setImageResource(R.drawable.ic_view_type_large)
                gridLayoutManager.spanCount = 1
                adapter.notifyDataSetChanged()
            }else{
                adapter.viewType = VIEW_TYPE_SMALL
                viewTypeChangerBtn.setImageResource(R.drawable.ic_grid)
                gridLayoutManager.spanCount = 2
                adapter.notifyDataSetChanged()

            }



        }
        sortBtn.setOnClickListener {
        val dialog =MaterialAlertDialogBuilder(this)
            .setSingleChoiceItems(R.array.sortTitlesArray,viewModel.sort) { dialog, selectedSortIndex ->
            viewModel.onSelectedSortChangeByUser(selectedSortIndex)
            dialog.dismiss()
            }.setTitle(R.string.sort)

            dialog.show()
        }
        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }

        viewModel.selectedSortLiveData.observe(this){
            selectedSortTitleTv.text = getString(it)
        }




    }

    override fun OnProductClick(product: Product) {
        startActivity(Intent(this,ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA,product)
        })
    }

    override fun OnFavoriteBtnClick(product: Product) {
    viewModel

    }
}