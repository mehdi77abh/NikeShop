package com.example.nikeshop.feature.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nikeshop.NikeFragment
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.data.DataClass.*
import com.example.nikeshop.feature.common.ProductListAdapter
import com.example.nikeshop.feature.common.VIEW_TYPE_ROUND
import com.example.nikeshop.feature.product.ProductDetailActivity
import com.example.nikeshop.feature.product.list.ProductListActivity
import com.example.nikeshop.feature.search.SearchActivity
import com.sevenlearn.nikestore.common.convertDpToPixel
import com.sevenlearn.nikestore.common.hideKeyboard
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class HomeFragment : NikeFragment(), ProductListAdapter.ProductEventListener,
    View.OnClickListener, SearchListAdapter.SearchEventListener {

    val viewModel: HomeViewModel by viewModel()

    val latestProductListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    val popularProductListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    val lowerPriceProductListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    val higherPriceProductListAdapter: ProductListAdapter by inject { parametersOf(VIEW_TYPE_ROUND) }
    val searchListAdapter: SearchListAdapter by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapters()
        setObservers()
        setSearchEditText()
        viewLatestProductBtn.setOnClickListener(this)
        viewPopularProductBtn.setOnClickListener(this)
        viewHigherPriceProductBtn.setOnClickListener(this)
        viewLowerPriceProductBtn.setOnClickListener(this)

        latestProductListAdapter.productEventListener = this
        popularProductListAdapter.productEventListener = this
        higherPriceProductListAdapter.productEventListener = this
        lowerPriceProductListAdapter.productEventListener = this




    }
    private fun setObservers(){
        viewModel.latestProductsListLiveData.observe(viewLifecycleOwner) {
            latestProductListAdapter.productList = it as ArrayList<Product>
        }
        viewModel.popularProductsListLiveData.observe(viewLifecycleOwner) {
            popularProductListAdapter.productList = it as ArrayList<Product>
        }
        viewModel.higherPriceProductsListLiveData.observe(viewLifecycleOwner) {
            higherPriceProductListAdapter.productList = it as ArrayList<Product>

        }
        viewModel.lowerPriceProductsListLiveData.observe(viewLifecycleOwner) {
            lowerPriceProductListAdapter.productList = it as ArrayList<Product>
        }
        viewModel.progressBarLiveData.observe(viewLifecycleOwner) {
            setProgressIndicator(it)
        }

        viewModel.bannerLiveData.observe(viewLifecycleOwner) {
            val bannerSliderAdapter = BannerSliderAdapter(this, it)
            bannerSliderViewPager.adapter = bannerSliderAdapter
            val bannerSliderWidth =
                (bannerSliderViewPager.measuredWidth - convertDpToPixel(32f, context))
            val height = ((bannerSliderWidth * 173) / 328).toInt()
            val layoutParams = bannerSliderViewPager.layoutParams

            if (viewModel.viewPagerHeight > 0)
                layoutParams.height = viewModel.viewPagerHeight.toInt()
            else {
                layoutParams.height = height
                viewModel.viewPagerHeight = height.toFloat()
            }

            bannerSliderViewPager.layoutParams = layoutParams

            sliderIndicator.setViewPager2(bannerSliderViewPager)

        }

        viewModel.searchProductsListLiveData.observe(viewLifecycleOwner) {
            searchListAdapter.productList = it as ArrayList<Product>
        }
    }
    private fun setSearchEditText() {
        searchEt.setOnFocusChangeListener { v, hasFocus ->
            cancelSearchBtn.visibility = if (hasFocus) View.VISIBLE else View.GONE


        }


        cancelSearchBtn.setOnClickListener {
            searchEt.clearFocus()
            it.hideKeyboard()
            cancelSearchBtn.visibility =View.GONE

        }


        searchListAdapter.eventListener = this


    }

    private fun setAdapters() {
        latestProductsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularProductsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        highPriceProductsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        lowPriceProductsRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        latestProductsRv.adapter = latestProductListAdapter
        popularProductsRv.adapter = popularProductListAdapter
        highPriceProductsRv.adapter = higherPriceProductListAdapter
        lowPriceProductsRv.adapter = lowerPriceProductListAdapter

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.viewLatestProductBtn -> startActivity(
                Intent(
                    requireContext(),
                    ProductListActivity::class.java
                ).apply {
                    putExtra(EXTRA_KEY_DATA, SORT_LATEST)
                })
            R.id.viewPopularProductBtn -> startActivity(
                Intent(
                    requireContext(),
                    ProductListActivity::class.java
                ).apply {
                    putExtra(EXTRA_KEY_DATA, SORT_POPULAR)
                })
            R.id.viewHigherPriceProductBtn -> startActivity(
                Intent(
                    requireContext(),
                    ProductListActivity::class.java
                ).apply {
                    putExtra(EXTRA_KEY_DATA, SORT_PRICE_ASC)
                })
            R.id.viewLowerPriceProductBtn -> startActivity(
                Intent(
                    requireContext(),
                    ProductListActivity::class.java
                ).apply {
                    putExtra(EXTRA_KEY_DATA, SORT_PRICE_DESC)
                })
        }
    }

    override fun OnProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun OnFavoriteBtnClick(product: Product) {
        viewModel.addOrDeleteProductFromFavorites(product)
    }

    override fun onSearchedProductClick(product: Product) {
        startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, product)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllProducts()
    }



}
