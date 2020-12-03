package com.example.week3day5.models

import org.json.JSONObject
import java.io.Serializable

data class User(
    var _id: String,
    var firstName: String,
    var email: String,
    var password: String,
    var mobile: String,
    //use for order post and get only
    var name: String = ""
): Serializable {

    companion object{
        const val KEY_ID = "userId"
        const val KEY_NAME = "firstName"
        const val KEY_EMAIL = "email"
        const val KEY_PW = "password"
        const val KEY_MOBILE = "mobile"

        //key in server response
        const val KEY_USER = "user"
        const val KEY_TOKEN = "token"

        fun convertToJsonObj(name: String, email:String, pw: String, mobile: String): JSONObject{
            val data = HashMap<String,String>()
            data[KEY_NAME] = name
            data[KEY_EMAIL] = email
            data[KEY_PW] = pw
            data[KEY_MOBILE] = mobile

            return JSONObject(data as Map<String,String>)
        }
    }
    constructor(): this("", "", "", "", ""){
        name = firstName
    }

    fun setName(){
        name = firstName
    }
}