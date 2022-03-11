package com.example.nikeshop.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.data.DataClass.Banner
import com.example.nikeshop.serivce.ImageLoadingService
import com.example.nikeshop.customView.NikeImageView
import org.koin.android.ext.android.inject
import java.lang.IllegalStateException

class BannerFragment : Fragment() {
    val imageLoadingService: ImageLoadingService by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imageView =
            inflater.inflate(R.layout.fragment_banner, container, false) as NikeImageView
        val banner = requireArguments().getParcelable<Banner>(EXTRA_KEY_DATA)?:throw IllegalStateException("Banner Cannot Be null")
        imageLoadingService.load(imageView,banner.image)
        return imageView
    }

    companion object {
        fun newInstance(banner: Banner): BannerFragment {
            return BannerFragment().apply {
                arguments = Bundle().apply {
                    this.putParcelable(EXTRA_KEY_DATA, banner)

                }
            }
        }
    }


}