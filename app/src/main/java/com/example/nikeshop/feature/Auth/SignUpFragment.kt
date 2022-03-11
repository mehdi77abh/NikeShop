package com.example.nikeshop.feature.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikeshop.NikeFragment
import com.example.nikeshop.R
import com.example.nikeshop.common.NikeCompletableObserver
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.signUpLinkBtn
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.koin.android.viewmodel.ext.android.viewModel

class SignUpFragment : NikeFragment() {
    val viewModel: AuthViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn.setOnClickListener {
            viewModel.signUp(signUpEmailEt.text.toString(), signUpPasswordEt.text.toString())
                .asyncNetworkRequest().subscribe(object :NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        requireActivity().finish()
                    }
                })
        }
        loginLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,LoginFragment()).commit()

        }



    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}