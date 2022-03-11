package com.example.nikeshop.feature.addComment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_ID
import com.example.nikeshop.common.NikeCompletableObserver
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_add_comment.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AddCommentActivity : NikeActivity() {
    private val compositeDisposable = CompositeDisposable()
    val viewModel: AddCommentViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_ID
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_comment)

        submitCommentBtn.setOnClickListener {
            viewModel.addComment(commentTitleEt.text.toString(), commentContentEt.text.toString())
                .asyncNetworkRequest().subscribe(object :NikeCompletableObserver(compositeDisposable){
                    override fun onComplete() {
                        showSnackBar(getString(R.string.successfulAddComment))
                        submitCommentBtn.visibility = View.INVISIBLE
                        finish()
                    }
                })
        }
        addCommentToolbar.onBackBtnClickListener = View.OnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}