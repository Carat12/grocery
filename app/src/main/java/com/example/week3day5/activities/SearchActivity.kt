package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterProduct
import com.example.week3day5.app.Config
import com.example.week3day5.app.Divider
import com.example.week3day5.app.EndPoints
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.models.Product
import com.example.week3day5.models.ResponseSearch
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_update_order_item_count.view.*
import kotlinx.android.synthetic.main.tool_bar_search.*
import org.json.JSONObject

class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    AdapterProduct.onAdapterListener {

    lateinit var adapter: AdapterProduct
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        init()
    }

    override fun onStart() {
        super.onStart()
        invalidateOptionsMenu()
        adapter.notifyDataSetChanged()
    }

    private fun init() {
        //tool bar
        setSupportActionBar(tool_bar_search)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        search_view.onActionViewExpanded()
        search_view.setOnQueryTextListener(this)
        //recycler view
        dbHelper = DBHelper(this)
        adapter = AdapterProduct(this, dbHelper)
        adapter.setOnAdapterListener(this)
        recycler_view_search_results.adapter = adapter
        recycler_view_search_results.layoutManager = LinearLayoutManager(this)
        recycler_view_search_results.addItemDecoration(Divider.getDivider(this, DividerItemDecoration.VERTICAL, R.drawable.divider_product))
        //
        progress_bar.isVisible = false
        text_view_no_results.isVisible = false
    }

    private fun getSearchResult(query: String?) {
        val request = StringRequest(Request.Method.GET, EndPoints.getProductSearchUrl(query),
            {
                val response = Gson().fromJson(it, ResponseSearch::class.java)
                adapter.setData(response.data)
                progress_bar.isVisible = false
                text_view_no_results.isVisible = response.data.isEmpty()
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(applicationContext, JSONObject(response).getString(Config.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return MenuManager.menuInit(this, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        //search_view.clearFocus()
        progress_bar.isVisible = true
        getSearchResult(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onItemClicked(itemView: View, position: Int) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra(Product.KEY_VALUE, adapter.getItemData(position))
        startActivity(intent)
    }

    override fun onAddButtonClicked(itemView: View, position: Int) {
        dbHelper.addOrderItem(adapter.getItemData(position), 1)
        adapter.notifyItemChanged(position)
        invalidateOptionsMenu()
    }

    override fun onIncreaseButtonClicked(itemView: View, position: Int) {
        val quantityInCart = itemView.text_view_count.text.toString().toInt()
        dbHelper.updateOrderItemCount(adapter.getItemData(position)._id, quantityInCart + 1)
        adapter.notifyItemChanged(position)
    }

    override fun onDecreaseButtonClicked(itemView: View, position: Int) {
        val quantityInCart = itemView.text_view_count.text.toString().toInt()
        val product = adapter.getItemData(position)
        if (quantityInCart == 1) {
            dbHelper.deleteOrderItem(product._id)
            invalidateOptionsMenu()
        } else
            dbHelper.updateOrderItemCount(product._id, quantityInCart - 1)
        adapter.notifyItemChanged(position)
    }
}