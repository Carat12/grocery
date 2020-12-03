package com.example.week3day5.models

import java.io.Serializable

data class OrderItem(
    val _id: String,
    val productName: String,
    val image: String,
    val price: Double,
    var mrp: Double,
    var quantity: Int
): Serializable {
    companion object {
        const val ADD_TO_CART = "Add to cart"
        const val IN_CART = "In cart"
        const val UPDATE_QUANTITY = "Update quantity"
        const val REMOVE = "Remove"
    }

    /*constructor(product: Product, quantity: Int) : this(
        product._id,
        product.productName,
        product.image,
        product.price,
        product.mrp,
        quantity
    )*/
}