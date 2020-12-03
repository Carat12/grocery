package com.example.week3day5.activities

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterOrderItem
import com.example.week3day5.app.Config
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.Address
import com.example.week3day5.models.Order
import com.example.week3day5.models.OrderSummary
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.layout_item_address.*
import kotlinx.android.synthetic.main.layout_item_summary.*
import kotlinx.android.synthetic.main.tool_bar.*

class PaymentActivity : AppCompatActivity() {

    lateinit var adapter: AdapterOrderItem
    lateinit var address: Address
    lateinit var orderSummary: OrderSummary
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        init()
    }

    private fun init() {
        dbHelper = DBHelper(this)
        //tool bar
        tool_bar.title = Config.TITLE_PAYMENT
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //shipment info
        address = intent.getSerializableExtra(Address.KEY_ADDRESS) as Address
        text_view_name.text = SessionManager.userLoggedIn.firstName
        text_view_street.text = address.getAddressPartOne()
        text_view_city_state_zipcode.text = address.getAddressPartTwo()

        //recycler view
        adapter = AdapterOrderItem(this)
        recycler_view_product.adapter = adapter
        recycler_view_product.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        getOrderItemData()

        //calculate
        orderSummary = dbHelper.calculateOrder()
        text_view_subtotal.text = "\$${orderSummary.ourPrice}"
        text_view_discount.text = "-\$${orderSummary.discount}"
        text_view_delivery.text = "\$${OrderSummary.DEFAULT_DELIVERY_FEE}"
        text_view_delivery.paintFlags = if (orderSummary.deliveryCharges == 0.0) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
        text_view_total.text = "\$${orderSummary.orderAmount}"

        //place order
        btn_place_order.setOnClickListener {
            val intent = Intent(this, OrderConfirmActivity::class.java)
            intent.putExtra(Address.KEY_ADDRESS, address)
            intent.putExtra(OrderSummary.KEY_ORDER_SUMMARY, orderSummary)
            startActivity(intent)
        }
    }

    fun getOrderItemData() {
        val dbHelper = DBHelper(this)
        adapter.setData(dbHelper.readAllOrderItems())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }
}