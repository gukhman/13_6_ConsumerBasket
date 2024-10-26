package com.example.a13_6_consumerbasket

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.apply
import kotlin.text.isNotEmpty

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "BASKET_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "goods_table"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_WEIGHT = "weight"
        const val KEY_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_WEIGHT + " TEXT, " +
                KEY_PRICE + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db) // Пересоздание таблицы после удаления
    }

    fun addName(name: String, weight: String, price: String): Boolean {
        if (name.length > 1 && weight.isNotEmpty() && price.isNotEmpty()) {
            val values = ContentValues().apply {
                put(KEY_NAME, name)
                put(KEY_WEIGHT, weight)
                put(KEY_PRICE, price)
            }

            val db = this.writableDatabase
            val result = db.insert(TABLE_NAME, null, values)
            db.close()

            return result != -1L // Возвращаем true, если вставка успешна
        }
        return false
    }

    fun getInfo(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close() // Закрываем базу после удаления
    }
}