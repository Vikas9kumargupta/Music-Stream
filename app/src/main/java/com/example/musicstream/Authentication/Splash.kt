package com.example.musicstream.Authentication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicstream.R
import com.example.musicstream.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            // Start your main activity here
            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
            // Close the splash activity
            finish()
        }, 2500)
    }
}