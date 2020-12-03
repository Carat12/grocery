package com.example.week3day5.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.adapters.AdapterCatImg
import com.example.week3day5.adapters.AdapterCategory
import com.example.week3day5.app.Config
import com.example.week3day5.app.Divider
import com.example.week3day5.app.EndPoints
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.Category
import com.example.week3day5.models.ResponseCategory
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home_content.*
import kotlinx.android.synthetic.main.navigation_header.view.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    AdapterCategory.onAdapterListener {

    lateinit var adapterCat: AdapterCategory
    lateinit var adapterCatImg: AdapterCatImg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    override fun onStart() {
        super.onStart()
        invalidateOptionsMenu()
    }

    private fun init() {
        setToolBar()
        setNavigationView()
        //search view
        layout_search_view.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        //view pager
        adapterCatImg = AdapterCatImg(this)
        view_pager_img.adapter = adapterCatImg

        //recycler view
        adapterCat = AdapterCategory(this)
        adapterCat.setOnAdapterListener(this)
        recycler_view_category.adapter = adapterCat
        recycler_view_category.layoutManager = GridLayoutManager(this, 2)

        recycler_view_category.addItemDecoration(
            Divider.getDivider(
                this,
                DividerItemDecoration.VERTICAL,
                R.drawable.divider_category_vertical
            )
        )
        recycler_view_category.addItemDecoration(
            Divider.getDivider(
                this,
                DividerItemDecoration.HORIZONTAL,
                R.drawable.divider_category_horizontal
            )
        )

        getCategory()
    }

    private fun setToolBar() {
        tool_bar.title = Config.TITLE_APP
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setNavigationView() {
        //val toggle = ActionBarDrawerToggle(this, drawer_layout, tool_bar, 0, 0)
        //drawer_layout.addDrawerListener(toggle)
        //toggle.syncState()

        val nav_header = nav_view.getHeaderView(0)
        if (SessionManager.isLoggedIn) {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_account_circle_24)

            val user = SessionManager.userLoggedIn
            nav_header.text_view_initial.text = user.firstName.substring(0, 1).toUpperCase()
            nav_header.text_view_name.text = "Hello, ${user.firstName}"
            nav_header.text_view_email.text = user.email
        } else {
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

            nav_header.text_view_name.text = "Hello"
            nav_header.text_view_email.text = "You have not signed in"
        }
        MenuManager.navigationMenuInit(nav_view.menu)
        nav_view.setNavigationItemSelectedListener(this)
    }

    fun getCategory() {
        val request = StringRequest(Request.Method.GET, EndPoints.getCategoryUrl(),
            {
                val responseCat = Gson().fromJson(it, ResponseCategory::class.java)
                adapterCat.setData(responseCat.data)
                adapterCatImg.setData(responseCat.data)
            },
            {
                val response = String(it.networkResponse.data)
                Toast.makeText(
                    applicationContext,
                    JSONObject(response).getString(Config.KEY_ERROR_MESSAGE),
                    Toast.LENGTH_SHORT
                ).show()
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
        MenuManager.menuOptionSelected(this, item)
        if (item.itemId == android.R.id.home) {
            if (drawer_layout.isDrawerOpen(GravityCompat.START))
                drawer_layout.closeDrawer(GravityCompat.START)
            else
                drawer_layout.openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_login -> startActivity(Intent(this, LoginActivity::class.java))
            R.id.menu_account -> {
                if (SessionManager.isLoggedIn)
                    startActivity(Intent(this, AccountActivity::class.java))
                else
                    startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.menu_orders -> {
                if (SessionManager.isLoggedIn)
                    startActivity(Intent(this, OrderHistoryActivity::class.java))
                else
                    startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.menu_logout -> {
                val sm = SessionManager(this)
                sm.removeUserFromSp()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START))
            drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onItemClicked(view: View, position: Int) {
        val intent = Intent(this, SubCategoryActivity::class.java)
        intent.putExtra(Category.KEY_VALUE, adapterCat.getItemData(position))
        startActivity(intent)
    }

}