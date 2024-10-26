package com.example.a13_6_consumerbasket

import android.os.Bundle

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindowInsets(R.id.main)
        setupToolbar(R.id.toolbar, false)
    }
}