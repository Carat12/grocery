package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_order_confirm.*
import org.json.JSONObject

class OrderConfirmActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirm)

        init()
    }

    private fun init() {
        placeOrder()

        btn_return_to_home.setOnClickListener(this)
        btn_to_order.setOnClickListener(this)
    }

    fun placeOrder() {
        val address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address
        val orderSummary = intent.getSerializableExtra(OrderSummary.KEY_ORDER_SUMMARY) as OrderSummary
        val orderItems = DBHelper(this).readAllOrderItems()
        val user = SessionManager.userLoggedIn

        val order = Order(Order.ORDER_COMPLETED, user._id, orderSummary, address, user, orderItems)
        val orderObj = JSONObject(Gson().toJson(order))
        //Log.d("scoups", orderObj.toString())

        val request = JsonObjectRequest(Request.Method.POST, EndPoints.getOrderPostUrl(), orderObj,
            {
                //Log.d("scoups", it.toString())
                progress_bar.visibility = View.GONE
                text_view_success.visibility = View.VISIBLE
                img_view_success.visibility = View.VISIBLE
                layout_success_btn.visibility = View.VISIBLE
                //clear cart
                DBHelper(this).clearOrderItem()
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(applicationContext, JSONObject(response).getString(Config.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_return_to_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }
            btn_to_order -> {
                startActivity(Intent(this, OrderHistoryActivity::class.java))
                //finishAffinity()
            }
        }
    }
}