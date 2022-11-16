package com.balajitech.librarymanagement.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.balajitech.librarymanagement.R


class SplashScreenActivity : AppCompatActivity() {
    var getStartedBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        getStartedBtn = findViewById<View>(R.id.GetStartedBtn) as Button


        //implementing OnClickListener
        getStartedBtn!!.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}