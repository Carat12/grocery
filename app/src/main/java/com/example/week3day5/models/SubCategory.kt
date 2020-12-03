package com.example.week3day5.models

import java.io.Serializable

data class SubCategory(
    val __v: Int,
    val _id: String,
    val catId: Int,
    val position: Int,
    val status: Boolean,
    val subDescription: String,
    val subId: Int,
    val subImage: String,
    val subName: String
): Serializable {
    companion object{
        const val KEY_VALUE = "SUBCAT"
    }
}