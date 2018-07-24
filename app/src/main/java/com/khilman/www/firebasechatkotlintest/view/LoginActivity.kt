package com.khilman.www.firebasechatkotlintest.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.khilman.www.firebasechatkotlintest.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.title = "FireChat Login"

    }
}
