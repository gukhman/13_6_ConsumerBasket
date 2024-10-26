package com.example.a13_6_consumerbasket

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar

class MainActivity : BaseActivity() {

    private val goodsList = mutableListOf<Good>()
    private val db = DBHelper(this, null)

    private lateinit var addBTN: Button
    private lateinit var clearBTN: Button
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

        //Добавление товара
        addBTN.setOnClickListener {

            val name = nameET.text.toString()
            val weight = weightET.text.toString()
            val price = priceET.text.toString()

            if (name.length > 1 && weight.isNotEmpty() && price.isNotEmpty()) {
                val isAdd = db.addName(name, weight, price)
                if (isAdd) {
                    clearET()

                    goodsList.add(Good(name, weight, price))
                    applyAdapter(goodsList)

                    Snackbar.make(it, "Товар $name добавлен", Snackbar.LENGTH_LONG).show()
                } else Snackbar.make(it, "Ошибка добавления в БД", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(it, "Введите все данные", Snackbar.LENGTH_LONG).show()
            }
        }

        //Очистка всех товаров
        clearBTN.setOnClickListener {
            clearET()
            db.removeAll()

            goodsList.clear()
            applyAdapter(goodsList)

            Snackbar.make(it, "Все товары удалены", Snackbar.LENGTH_LONG).show()
        }

        //Удаление 1 товара из чека
        goodsLV.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = goodsList[position]

            //обновим список товаров
            goodsList.remove(selectedItem)
            applyAdapter(goodsList)

            //обновляем инфо в БД
            db.removeAll()
            goodsList.forEach {
                db.addName(it.name, it.weight, it.price)
            }

            val rootView: View = findViewById(android.R.id.content)
            Snackbar.make(rootView, "Товар удален из чека", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun clearET() {
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    private fun applyAdapter(goodsList: MutableList<Good>) {
        goodsLV.adapter = GoodsAdapter(this, goodsList)
    }

    private fun init() {
        addBTN = findViewById(R.id.addBTN)
        clearBTN = findViewById(R.id.clearBTN)
        nameET = findViewById(R.id.nameTV)
        weightET = findViewById(R.id.weightTV)
        priceET = findViewById(R.id.priceTV)
        goodsLV = findViewById(R.id.goodsLV)
    }
}