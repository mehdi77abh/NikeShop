package com.example.nikeshop.feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikeshop.NikeFragment
import com.example.nikeshop.R
import com.example.nikeshop.data.DataClass.TokenContainer
import com.example.nikeshop.feature.Auth.AuthActivity
import com.example.nikeshop.feature.Favorites.FavoriteListActivity
import com.example.nikeshop.feature.OrderHistory.OrderHistoryActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : NikeFragment() {
    private val viewModel: ProfileViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteBtn.setOnClickListener {
            startActivity(Intent(requireContext(),FavoriteListActivity::class.java))
        }
        orderHistoryBtn.setOnClickListener {
            if(!TokenContainer.token.isNullOrEmpty()){
                startActivity(Intent(requireActivity(),OrderHistoryActivity::class.java))
            }else
                showToast(getString(R.string.loginForOrderHistory))
        }


    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    private fun checkAuthState() {
        if (viewModel.isSignedIn) {
            authBtn.text =getString(R.string.signOut)
            authBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sign_out,0,0,0)
            usernameTv.text=viewModel.userName
            authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }
        }else {
            authBtn.text = getString(R.string.signIn)
            authBtn.setOnClickListener {
                startActivity(Intent(requireContext(),AuthActivity::class.java))
            }
            authBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sign_in,0,0,0)
            usernameTv.text =getString(R.string.guestUser)
        }
    }


}