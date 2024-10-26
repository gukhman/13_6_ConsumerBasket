package com.example.a13_6_consumerbasket

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar

class MainActivity : BaseActivity() {

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

        addBTN.setOnClickListener {
            outputTV.text = ""

            val name = nameET.text.toString()
            val phone = phoneET.text.toString()

            if (name.length > 1 && phone.isNotEmpty()) {
                val isAdd = db.addName(name, phone, selectedRole)
                if (isAdd) {
                    nameET.text.clear()
                    phoneET.text.clear()
                    roleSpinner.setSelection(0)

                    Snackbar.make(it, "Сотрудник $name добавлен", Snackbar.LENGTH_LONG).show()
                } else Snackbar.make(it, "Ошибка добавления в БД", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(it, "Введите все данные", Snackbar.LENGTH_LONG).show()
            }
        }

        readBTN.setOnClickListener {
            outputTV.text = ""

            val cursor = db.getInfo()
            var rowText = ""
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()

                rowText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + " | " +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n" +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE))

                outputTV.append("$rowText\n-------------------\n")

                Snackbar.make(it, "Данные прочитаны", Snackbar.LENGTH_LONG).show()
            } else
                Snackbar.make(
                    it,
                    "База данных отсутствует, давайте создадим ее",
                    Snackbar.LENGTH_LONG
                ).show()

            while (cursor!!.moveToNext()) {
                rowText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME)) + " | " +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_PHONE)) + "\n" +
                        cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ROLE))

                outputTV.append("$rowText\n-------------------\n")
            }
            cursor.close()
        }

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