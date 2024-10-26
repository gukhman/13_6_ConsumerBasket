package com.example.a13_6_consumerbasket

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
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

        initViews()
        setupListeners()
    }

    //Инициализация
    private fun initViews() {
        addBTN = findViewById(R.id.addBTN)
        clearBTN = findViewById(R.id.clearBTN)
        nameET = findViewById(R.id.nameET)
        weightET = findViewById(R.id.weightET)
        priceET = findViewById(R.id.priceET)
        goodsLV = findViewById(R.id.goodsLV)
    }

    //Слушатели нажатия
    private fun setupListeners() {
        addBTN.setOnClickListener { handleAddButtonClick(it) }
        clearBTN.setOnClickListener { handleClearButtonClick(it) }
        goodsLV.setOnItemClickListener { _, _, position, _ ->
            showEditOrDeleteDialog(position)
        }
    }

    //Кнопка добавления
    private fun handleAddButtonClick(view: View) {
        val name = nameET.text.toString()
        val weight = weightET.text.toString()
        val price = priceET.text.toString()

        if (name.length > 1 && weight.isNotEmpty() && price.isNotEmpty()) {
            //добавляем в БД
            if (db.addName(name, weight, price)) {

                //Очищаем поля
                clearInputFields()

                //меняем listView
                goodsList.add(Good(name, weight, price))
                refreshGoodsList()

                showSnackbar(view, "Товар $name добавлен")
            } else {
                showSnackbar(view, "Ошибка добавления в БД")
            }
        } else {
            showSnackbar(view, "Введите все данные")
        }
    }

    //кнопка очистки
    private fun handleClearButtonClick(view: View) {
        clearInputFields()
        db.removeAll()
        goodsList.clear()
        refreshGoodsList()
        showSnackbar(view, "Все товары удалены")
    }

    //Диалог выбора изменения/удаления товара
    private fun showEditOrDeleteDialog(position: Int) {
        val selectedItem = goodsList[position]

        AlertDialog.Builder(this)
            .setTitle("Выберите действие")
            .setMessage("Вы хотите изменить или удалить выбранный товар?")
            .setPositiveButton("Изменить") { dialog, _ ->
                showEditItemDialog(selectedItem)
                dialog.dismiss()
            }
            .setNegativeButton("Удалить") { dialog, _ ->
                deleteItem(selectedItem)
                dialog.dismiss()
            }
            .setNeutralButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    //Диалог изменения товара
    private fun showEditItemDialog(item: Good) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_item, null)
        val editName = dialogView.findViewById<EditText>(R.id.nameET)
        val editWeight = dialogView.findViewById<EditText>(R.id.weightET)
        val editPrice = dialogView.findViewById<EditText>(R.id.priceET)

        editName.setText(item.name)
        editWeight.setText(item.weight)
        editPrice.setText(item.price)

        AlertDialog.Builder(this)
            .setTitle("Изменить товар")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = editName.text.toString()
                val newWeight = editWeight.text.toString()
                val newPrice = editPrice.text.toString()

                if (newName.length > 1 && newWeight.isNotEmpty() && newPrice.isNotEmpty()) {
                    updateItem(item, newName, newWeight, newPrice)
                } else {
                    showSnackbar(dialogView, "Поля не должны быть пустыми")
                }
            }
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun updateItem(item: Good, newName: String, newWeight: String, newPrice: String) {
        db.updateItem(item, newName, newWeight, newPrice)
        item.name = newName
        item.weight = newWeight
        item.price = newPrice
        refreshGoodsList()
        showSnackbar(findViewById(android.R.id.content), "Товар изменен")
    }

    private fun deleteItem(item: Good) {
        goodsList.remove(item)
        db.deleteItem(item)
        refreshGoodsList()
        showSnackbar(findViewById(android.R.id.content), "Товар удален из чека")
    }

    private fun clearInputFields() {
        nameET.text.clear()
        weightET.text.clear()
        priceET.text.clear()
    }

    private fun refreshGoodsList() {
        goodsLV.adapter = GoodsAdapter(this, goodsList)
    }

    private fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}
