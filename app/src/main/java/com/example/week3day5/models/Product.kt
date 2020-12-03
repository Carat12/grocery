package com.example.week3day5.models

import java.io.Serializable
import kotlin.math.roundToInt

data class Product(
    val _id: String,
    val productName: String,
    val image: String,
    val price: Double,
    val mrp: Double,
    val description: String,
    val quantity: Int,
    val subId: Int,
    val catId: Int,
    val status: Boolean,
    val __v: Int,
    val created: String,
    val position: Int,
    val unit: String
) : Serializable {
    companion object {
        const val KEY_VALUE = "PRODUCT"

        fun roundPrice(price: Double): Double{
            return (price * 100).roundToInt() / 100.0
        }
    }
}