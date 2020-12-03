package com.example.week3day5.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.fragments.OrderHistoryDetailFragment
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.models.Order
import kotlinx.android.synthetic.main.activity_order_history_detail.*
import kotlinx.android.synthetic.main.tool_bar.*

class OrderHistoryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history_detail)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_ORDER_DETAILS
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //fragment
        val order = intent.getSerializableExtra(Order.KEY_ORDER) as Order
        val fragment = OrderHistoryDetailFragment.newInstance(order)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_order_history, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }
}