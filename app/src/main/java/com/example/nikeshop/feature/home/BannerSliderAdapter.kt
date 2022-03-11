package com.example.nikeshop.feature.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nikeshop.data.DataClass.Banner

class BannerSliderAdapter(fragment: Fragment,val banners:List<Banner>): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = banners.size


    override fun createFragment(position: Int): Fragment =BannerFragment.newInstance(banners[position])
}