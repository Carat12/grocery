package com.example.week3day5.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.week3day5.models.OrderItem
import com.example.week3day5.models.OrderSummary
import com.example.week3day5.models.Product

class DBHelper(var mContext: Context) :
    SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    private var db = writableDatabase

    companion object {
        const val DATABASE_NAME = "grocery"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "OrderItems"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "productName"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_PRICE = "price"
        const val COLUMN_MRP = "mrp"
        const val COLUMN_QUANTITY = "quantity"

        var totalOrderItem = 0
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "create table $TABLE_NAME (" +
                "$COLUMN_ID char(100) primary key," +
                "$COLUMN_NAME char(100)," +
                "$COLUMN_IMAGE char(100)," +
                "$COLUMN_PRICE double," +
                "$COLUMN_MRP double," +
                "$COLUMN_QUANTITY int)"
        db!!.execSQL(createTable)

        //Log.d("scoups", "onCreateDB")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun readAllOrderItems(): ArrayList<OrderItem> {
        val res: ArrayList<OrderItem> = ArrayList()

        //db = readableDatabase
        val query = "select * from $TABLE_NAME"
        val cursor = db.rawQuery(query, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                val price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                val mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))
                val quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))

                val orderItem = OrderItem(id, name, image, price, mrp, quantity)
                res.add(orderItem)
            } while (cursor.moveToNext())
            cursor.close()
        }
        totalOrderItem = res.size
        return res
    }

    fun addOrderItem(product: Product, quantity: Int) {
        val existQuantity = getOrderItemQuantity(product)
        if (existQuantity == 0) {

            val values = ContentValues()
            values.put(COLUMN_ID, product._id)
            values.put(COLUMN_NAME, product.productName)
            values.put(COLUMN_IMAGE, product.image)
            values.put(COLUMN_PRICE, product.price)
            values.put(COLUMN_MRP, product.mrp)
            values.put(COLUMN_QUANTITY, quantity)

            db.insert(TABLE_NAME, null, values)
            totalOrderItem++
        } else {
            updateOrderItemCount(product._id, existQuantity + quantity)
        }
    }

    fun getOrderItemQuantity(product: Product): Int {
        val columns = arrayOf(COLUMN_ID, COLUMN_QUANTITY)
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(product._id)

        val cursor = db.query(TABLE_NAME, columns, whereClause, whereArgs, null, null, null)
        val res = if (cursor != null && cursor.moveToFirst())
            cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
        else 0
        cursor.close()
        return res
    }

    fun updateOrderItemCount(id: String, quantity: Int) {
        if(quantity == 0)
            deleteOrderItem(id)
        else{
            val values = ContentValues()

            values.put(COLUMN_QUANTITY, quantity)

            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(id)
            db.update(TABLE_NAME, values, whereClause, whereArgs)
        }
    }

    fun deleteOrderItem(id: String) {
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id)

        db.delete(TABLE_NAME, whereClause, whereArgs)
        totalOrderItem--
    }

    fun clearOrderItem() {
        db.delete(TABLE_NAME, null, null)
        totalOrderItem = 0
    }

    fun calculateOrder(): OrderSummary {
        val orderItems = readAllOrderItems()
        var subTotal = 0.0
        var total = 0.0
        for (item in orderItems) {
            subTotal += item.mrp * item.quantity
            total += item.price * item.quantity
        }
        subTotal = Product.roundPrice(subTotal)
        total = Product.roundPrice(total)
        val discount = Product.roundPrice(subTotal - total)
        val deliveryFee = if(total >= 500) 0.0 else OrderSummary.DEFAULT_DELIVERY_FEE
        val orderAmount = total + deliveryFee
        return OrderSummary(total, subTotal, discount, deliveryFee, orderAmount)
    }
}