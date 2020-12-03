package com.example.week3day5.models

import java.io.Serializable

data class Category(
    val __v: Int,
    val _id: String,
    val catDescription: String,
    val catId: Int,
    val catImage: String,
    val catName: String,
    val position: Int,
    val slug: String,
    val status: Boolean
): Serializable {
    companion object{
        const val KEY_VALUE = "CATEGORY"

        const val CAT_CHICKEN = "Chicken Items"
        const val CAT_VEG = "Veg Items"
        const val CAT_FRUIT = "Fruit Items"
        const val CAT_BEEF = "Beef Item"
        const val CAT_SEHRI= "Sehri"
    }
}