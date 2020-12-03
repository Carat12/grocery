package com.example.week3day5.models

import org.json.JSONObject
import java.io.Serializable

data class Address(
    //val __v: Int,
    val _id: String,
    val city: String,
    val houseNo: String,
    val pincode: Int,
    val streetName: String,
    val type: String
): Serializable {
    companion object{
        const val KEY_ADDRESS = "shippingAddress"

        //keys used when post address
        const val KEY_USER_ID = "userId"
        const val KEY_TYPE = "type"
        const val KEY_STREET = "streetName"
        const val KEY_HOUSE_NO = "houseNo"
        const val KEY_CITY = "city"
        //const val KEY_COUNTRY = "country"
        const val KEY_PINCODE = "pincode"

        //type
        val TYPE_LIST = arrayListOf("Home", "Office", "Other")

        fun convertToJsonObj(userId: String, type: String, street: String, houseNo: String, city: String, pincode: Int): JSONObject {
            val data = HashMap<String, String>()
            data[KEY_USER_ID] = userId
            data[KEY_TYPE] = type
            data[KEY_STREET] = street
            data[KEY_HOUSE_NO] = houseNo
            data[KEY_CITY] = city
            //data[KEY_COUNTRY] = country
            data[KEY_PINCODE] = pincode.toString()

            return JSONObject(data as Map<String,String>)
        }
    }

    fun getAddressPartOne(): String{
        return "$streetName $houseNo"
    }

    fun getAddressPartTwo(): String{
        return "$city, $pincode"
    }
}