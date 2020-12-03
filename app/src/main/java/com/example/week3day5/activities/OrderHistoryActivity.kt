package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterOrders
import com.example.week3day5.app.Config
import com.example.week3day5.app.Divider
import com.example.week3day5.app.EndPoints
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.Order
import com.example.week3day5.models.ResponseOrder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_history.*
import kotlinx.android.synthetic.main.tool_bar.*

class OrderHistoryActivity : AppCompatActivity(), AdapterOrders.OnAdapterListener {

    lateinit var adapter: AdapterOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_ORDER_HISTORY
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //recycler view
        adapter = AdapterOrders(this)
        adapter.setOnAdapterListener(this)
        recycler_view_order_history.adapter = adapter
        recycler_view_order_history.layoutManager = LinearLayoutManager(this)
        val divider = Divider.getDivider(this, DividerItemDecoration.VERTICAL, R.drawable.divider_product)
        recycler_view_order_history.addItemDecoration(divider)

        getOrderHistory()
    }

    private fun getOrderHistory() {
        val userId = SessionManager.userLoggedIn._id
        val request = StringRequest(Request.Method.GET, EndPoints.getOrderGetUrl(userId),
            {
                val response = Gson().fromJson(it, ResponseOrder::class.java)
                adapter.setData(response.data)
                text_view_no_order.isVisible = response.data.isEmpty()
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(applicationContext, response, Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            }
        }
        return true
    }

    override fun onButtonClicked(view: View, position: Int) {
        val intent = Intent(this, OrderHistoryDetailActivity::class.java)
        val order = adapter.getItemData(position)
        intent.putExtra(Order.KEY_ORDER, order)
        startActivity(intent)
    }
}