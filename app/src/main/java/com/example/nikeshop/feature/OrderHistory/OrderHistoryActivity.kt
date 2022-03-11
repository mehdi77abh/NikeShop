package com.example.nikeshop.feature.OrderHistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import kotlinx.android.synthetic.main.activity_order_history.*
import org.koin.android.viewmodel.ext.android.viewModel

class OrderHistoryActivity : NikeActivity() {
    val viewModel: OrderHistoryViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        orderHistoryRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        viewModel.orderHistoryListLiveData.observe(this) {
            val orderHistoryListAdapter = OrderHistoryListAdapter(this, it)
            orderHistoryRv.adapter = orderHistoryListAdapter
        }
        viewModel.progressBarLiveData.observe(this){
            setProgressIndicator(it)
        }
        toolbarView.onBackBtnClickListener = View.OnClickListener {
            finish()
        }


    }
}