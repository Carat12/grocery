package com.example.week3day5.models

import java.io.Serializable

open class Order(
    val orderStatus: String,
    var userId: String,
    var orderSummary: OrderSummary,
    var shippingAddress: Address,
    var user: User,
    var products: ArrayList<OrderItem>,
    val _id: String? = null,
    val date: String? = null
): Serializable {
    companion object{
        const val ORDER_COMPLETED = "Completed"
        const val KEY_ORDER = "order"
    }
}

