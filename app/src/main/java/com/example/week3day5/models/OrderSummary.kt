package com.example.week3day5.models

import java.io.Serializable

data class OrderSummary(
    var totalAmount: Double,
    var ourPrice: Double,
    var discount: Double,
    var deliveryCharges: Double,
    var orderAmount: Double
): Serializable{
    companion object{
        const val KEY_ORDER_SUMMARY = "orderSummary"
        const val DEFAULT_DELIVERY_FEE = 50.0
    }
}
