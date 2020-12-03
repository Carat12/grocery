package com.example.week3day5.activities

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterCart
import com.example.week3day5.app.Config
import com.example.week3day5.app.Divider
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.OrderItem
import com.example.week3day5.models.OrderSummary
import com.example.week3day5.models.Product
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.item_cart.*
import kotlinx.android.synthetic.main.layout_item_summary.*
import kotlinx.android.synthetic.main.tool_bar.*
import kotlin.math.roundToInt

class CartActivity : AppCompatActivity(), AdapterCart.onAdapterListener {

    lateinit var adapter: AdapterCart
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        init()
    }

    private fun init() {
        dbHelper = DBHelper(this)
        //tool tab
        tool_bar.title = Config.TITLE_CART
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //recycler view
        adapter = AdapterCart(this)
        adapter.setOnAdapterListener(this)
        recycler_view_cart.adapter = adapter
        recycler_view_cart.layoutManager = LinearLayoutManager(this)
        recycler_view_cart.addItemDecoration(Divider.getDivider(this, DividerItemDecoration.VERTICAL, R.drawable.divider_product))

        updateData()

        //check out
        btn_check_out.setOnClickListener {
            if(SessionManager.isLoggedIn)
                startActivity(Intent(this, AddressActivity::class.java))
            else
                startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun updateData() {
        val data = dbHelper.readAllOrderItems()
        adapter.setData(data)
        //cart is empty or not
        if(data.isEmpty()){
            text_view_cart.isVisible = true
            layout_cart_summary.isVisible = false
            layout_cart_items.layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT
        }else{
            text_view_cart.isVisible = false
            layout_cart_summary.isVisible = true
            layout_cart_items.layoutParams.height = 0

            //calculate
            val orderSummary = dbHelper.calculateOrder()
            text_view_subtotal.text = "\$${orderSummary.ourPrice}"
            text_view_discount.text = "-\$${orderSummary.discount}"
            text_view_delivery.text = "\$${OrderSummary.DEFAULT_DELIVERY_FEE}"
            text_view_delivery.paintFlags = if(orderSummary.deliveryCharges == 0.0) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
            text_view_total.text = "\$${orderSummary.orderAmount}"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return MenuManager.menuInit(this, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }

    override fun onButtonClicked(view: View, position: Int) {
        //val dbHelper = DBHelper(this)
        val orderItem = adapter.getItemData(position)
        val quantity = orderItem.quantity
        when(view.id){
            R.id.btn_increment_count -> dbHelper.updateOrderItemCount(orderItem._id, quantity + 1)
            R.id.btn_decrement_count -> dbHelper.updateOrderItemCount(orderItem._id, quantity - 1)
            R.id.btn_remove_order_item -> dbHelper.deleteOrderItem(orderItem._id)
        }
        updateData()
    }
}