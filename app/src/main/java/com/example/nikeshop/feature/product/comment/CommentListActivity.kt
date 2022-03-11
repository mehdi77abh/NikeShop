package com.example.nikeshop.feature.product.comment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_ID
import com.example.nikeshop.data.DataClass.Comment
import com.example.nikeshop.feature.addComment.AddCommentActivity
import kotlinx.android.synthetic.main.activity_comment_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CommentListActivity : NikeActivity() {
    val commentListViewModel: CommentListViewModel by viewModel {
        parametersOf(
            intent.extras!!.getInt(
                EXTRA_KEY_ID
            )
        )
    }
    val commentAdapter = CommentAdapter(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_list)

        commentListViewModel.commentListLiveData.observe(this) {
            commentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            commentAdapter.commentList = it as ArrayList<Comment>
            commentsRv.adapter = commentAdapter
        }
        commentListViewModel.progressBarLiveData.observe(this) {
            setProgressIndicator(it)
        }
        commentListToolbar.onBackBtnClickListener = View.OnClickListener {
            finish()
        }


        insetCommentBtn.setOnClickListener {
        startActivity(Intent(this,AddCommentActivity::class.java))

        }

    }


}