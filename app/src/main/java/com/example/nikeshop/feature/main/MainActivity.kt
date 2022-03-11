package com.example.nikeshop.feature.main

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R
import com.example.nikeshop.common.FaNum
import com.example.nikeshop.data.DataClass.CartItemCount
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.sevenlearn.nikestore.common.convertDpToPixel
import com.sevenlearn.nikestore.common.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : NikeActivity() {

    private var currentNavController: LiveData<NavController>? = null
    val viewModel:MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }


    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationMain)

        val navGraphIds = listOf(R.navigation.home, R.navigation.cart, R.navigation.profile)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        //Whenever the selected controller changes,setup action bar
        /*controller.observe(this, Observer {
            setupActionBarWithNavController(it)
        })*/
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCartItemCountChangeEvent(cartItemCount: CartItemCount){
        val badge = bottomNavigationMain.getOrCreateBadge(R.id.cart)
        badge.badgeGravity = BadgeDrawable.BOTTOM_START

        badge.backgroundColor=MaterialColors.getColor(bottomNavigationMain,R.attr.colorPrimary)
        badge.verticalOffset = convertDpToPixel(10f,this).toInt()
        badge.number = FaNum.convert(cartItemCount.count.toString()).toInt()
        badge.isVisible = cartItemCount.count >0

    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItemCounts()

    }

}