package com.example.nikeshop.feature.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikeshop.R
import com.example.nikeshop.feature.main.MainActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Timer("startSplash",false).schedule(1000){
            startActivity(Intent(this@SplashScreenActivity,MainActivity::class.java))
            this@SplashScreenActivity.finish()

        }

    }
}