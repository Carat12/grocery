package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.week3day5.R
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.SessionManager

class SplashActivity : AppCompatActivity() {

    private val DELAY_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            checkLogIn()
            checkOrderItem()
        }, DELAY_TIME)
    }

    private fun checkLogIn(){
        SessionManager(this).checkLogIn()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun checkOrderItem(){
        val dbHelper = DBHelper(this)
        dbHelper.readAllOrderItems()
        //Log.d("scoups", DBHelper.totalOrderItem.toString())
    }
}