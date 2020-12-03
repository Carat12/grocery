package com.example.week3day5.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterSubCategory
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.database.DBHelper
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.models.Category
import com.example.week3day5.models.ResponseSubCategory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class SubCategoryActivity : AppCompatActivity() {

    lateinit var category: Category
    lateinit var adapterSubCat: AdapterSubCategory
    lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)

        init()
    }

    override fun onStart() {
        super.onStart()
        invalidateOptionsMenu()
    }

    private fun init() {
        category = intent.getSerializableExtra(Category.KEY_VALUE) as Category
        dbHelper = DBHelper(this)
        //tool bar
        tool_bar.title = category.catName
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //view pager
        adapterSubCat = AdapterSubCategory(supportFragmentManager)
        view_pager_subcat.adapter = adapterSubCat
        tab_layout_subcat.setupWithViewPager(view_pager_subcat)

        getSubCategory()
    }

    //get subcategory
    private fun getSubCategory(){
        val request = StringRequest(Request.Method.GET, EndPoints.getSubCategoryUrl(category.catId),
            {
                val responseSubCat = Gson().fromJson(it, ResponseSubCategory::class.java)
                adapterSubCat.setData(responseSubCat.data)
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return MenuManager.updateCartItemQuantity(menu!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }
}