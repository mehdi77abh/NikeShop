package com.example.nikeshop.feature.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.nikeshop.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchEt.isSelected = true
        searchEt.setOnFocusChangeListener { view, focus ->


        }


    }
}