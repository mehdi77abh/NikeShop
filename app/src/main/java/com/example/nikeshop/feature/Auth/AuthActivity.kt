package com.example.nikeshop.feature.Auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikeshop.NikeActivity
import com.example.nikeshop.R

class AuthActivity : NikeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer,LoginFragment())
        }.commit()
    }
}