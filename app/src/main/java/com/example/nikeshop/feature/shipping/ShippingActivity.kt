package com.example.nikeshop.feature.shipping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.EXTRA_KEY_DATA
import com.example.nikeshop.common.EXTRA_KEY_ID
import com.example.nikeshop.common.NikeSingleObserver
import com.example.nikeshop.data.DataClass.PurchaseDetail
import com.example.nikeshop.feature.cart.CartItemAdapter
import com.sevenlearn.nikestore.common.asyncNetworkRequest
import com.sevenlearn.nikestore.common.openBrowserInCustomTabs
import com.sevenlearn.nikestore.data.SubmitOrderResult
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_shipping.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.lang.IllegalStateException

class ShippingActivity : NikeActivity() {

    val viewModel: ShippingViewModel by viewModel()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipping)

        val purchaseDetail = intent.getParcelableExtra<PurchaseDetail>(EXTRA_KEY_DATA)
            ?: throw IllegalStateException("Purchase Detail Cannot Be Null")

        val viewHolder = CartItemAdapter.PurchaseDetailViewHolder(purchaseDetailView)

        viewHolder.bind(purchaseDetail.total_price, purchaseDetail.shipping_cost,
            purchaseDetail.payable_price)

        val onClick = View.OnClickListener {
            viewModel.submit(
                firstNameEt.text.toString(), lastNameEt.text.toString(),
                postalCodeEt.text.toString(), phoneNumberEt.text.toString(), addressEt.text.toString(),
                if (it.id == R.id.onlineBtn) PAYMENT_METHOD_ONLINE else PAYMENT_METHOD_COD)
                .asyncNetworkRequest()
                .subscribe(object : NikeSingleObserver<SubmitOrderResult>(compositeDisposable) {
                    override fun onSuccess(t: SubmitOrderResult) {
                        Timber.i(t.toString())
                        if (t.bank_gateway_url.isNotEmpty()) {
                            openBrowserInCustomTabs(this@ShippingActivity, t.bank_gateway_url)
                        } else
                            startActivity(Intent(
                                this@ShippingActivity, CheckoutActivity::class.java
                            ).apply {
                                putExtra(EXTRA_KEY_ID, t.order_id)

                            })
                        finish()
                    }
                })
        }
        onlineBtn.setOnClickListener(onClick)
        codBtn.setOnClickListener(onClick)
        toolbarView.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}