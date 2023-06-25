package com.apps.heavengreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide // Jika menggunakan Glide

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 5000 // Durasi splash screen dalam milidetik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageViewSplash = findViewById<ImageView>(R.id.loader)
        Glide.with(this)
            .load(R.drawable.loading)
            .into(imageViewSplash)


        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
