package com.example.a13_6_consumerbasket

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView

class MainActivity : BaseActivity() {

    private lateinit var addBTN: Button
    private lateinit var nameET: EditText
    private lateinit var weightET: EditText
    private lateinit var priceET: EditText
    private lateinit var goodsLV: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWindowInsets(R.id.main)
        setupToolbar(R.id.toolbar, false)

        init()

        addBTN.setOnClickListener {

        }

    }

    private fun init() {
        addBTN = findViewById(R.id.addBTN)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        goodsLV = findViewById(R.id.goodsLV)
    }
}