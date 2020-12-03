package com.example.week3day5.models

data class ResponseCategory(
    val count: Int,
    val data: ArrayList<Category>,
    val error: Boolean
)

data class ResponseSubCategory(
    val count: Int,
    val data: ArrayList<SubCategory>,
    val error: Boolean
)

data class ResponseProduct(
    val count: Int,
    val data: ArrayList<Product>,
    val error: Boolean
)

data class ResponseAddress(
    val count: Int,
    val data: ArrayList<Address>,
    val error: Boolean
)

data class ResponseOrder(
    val count: Int,
    val data: ArrayList<Order>,
    val error: Boolean
)

data class ResponseSearch(
    val count: Int,
    val data: ArrayList<Product>,
    val error: Boolean
)