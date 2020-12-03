package com.example.week3day5.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.week3day5.activities.HomeActivity
import com.example.week3day5.models.User

class SessionManager(var mContext: Context) {

    private val FILE_NAME = "grocery_user"

    private val sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    private val editor = sp.edit()

    companion object{
        var isLoggedIn = false
        lateinit var userLoggedIn: User
    }

    fun checkLogIn(){
        isLoggedIn = sp.getString(User.KEY_TOKEN, null) != null
        val id = sp.getString(User.KEY_ID,"")!!
        val firstName = sp.getString(User.KEY_NAME, "")!!
        val email = sp.getString(User.KEY_EMAIL, "")!!
        val mobile = sp.getString(User.KEY_MOBILE,"")!!
        userLoggedIn = User(id, firstName, email, "", mobile, firstName)
    }

    fun addUserToSp(user: User, token: String){
        editor.putString(User.KEY_ID, user._id)
        editor.putString(User.KEY_NAME, user.firstName)
        editor.putString(User.KEY_EMAIL, user.email)
        editor.putString(User.KEY_PW, user.password)
        editor.putString(User.KEY_MOBILE, user.mobile)
        editor.putString(User.KEY_TOKEN, token)
        editor.apply()

        userLoggedIn = user
        isLoggedIn = true
    }

    fun removeUserFromSp(){
        editor.clear().apply()
        isLoggedIn = false
        userLoggedIn = User()
        mContext.startActivity(Intent(mContext, HomeActivity::class.java))
        (mContext as Activity).finishAffinity()
    }
}