package com.example.nikeshop.feature.shipping

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_ID
import com.example.nikeshop.feature.OrderHistory.OrderHistoryActivity
import com.example.nikeshop.feature.main.MainActivity
import com.sevenlearn.nikestore.common.formatPrice
import kotlinx.android.synthetic.main.activity_checkout.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckoutActivity : AppCompatActivity() {
    val viewModel: CheckoutViewModel by viewModel {
        val uri: Uri? = intent.data
        if (uri != null)
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else
            parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        viewModel.checkoutLiveData.observe(this) {
            orderPriceValueTv.text = formatPrice(it.payable_price.toString())
            purchaseStatusTv.text =
                if (it.purchase_success) "خرید با موفقیت انجام شد" else "خرید ناموفق"
            orderStatusTv.text = it.payment_status

        }
        orderHistoryBtn.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
            finish()
        }
        toolbarView.onBackBtnClickListener = View.OnClickListener {
            finish()
        }
        returnHomeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}