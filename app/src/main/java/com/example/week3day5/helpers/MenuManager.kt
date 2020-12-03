package com.example.week3day5.helpers

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import com.example.week3day5.R
import com.example.week3day5.activities.CartActivity
import com.example.week3day5.activities.HomeActivity
import com.example.week3day5.activities.LoginActivity
import com.example.week3day5.activities.SearchActivity
import com.example.week3day5.database.DBHelper
import kotlinx.android.synthetic.main.menu_option_cart.view.*

class MenuManager {

    companion object {

        fun menuInit(mContext: Context, menu: Menu?): Boolean {
            (mContext as Activity).menuInflater.inflate(R.menu.main_menu, menu)

            //change icon in cart activity
            if (mContext is CartActivity)
                menu!!.findItem(R.id.menu_cart).isVisible = false
            else
                menu!!.findItem(R.id.menu_home).isVisible = false

            //show cart count
            val menuCart = menu.findItem(R.id.menu_cart).actionView
            menuCart.img_btn_cart.setOnClickListener {
                mContext.startActivity(Intent(mContext,CartActivity::class.java))
            }
            updateCartItemQuantity(menu)

            return true
        }

        fun updateCartItemQuantity(menu: Menu): Boolean {
            val itemQuantity = DBHelper.totalOrderItem
            val menuCartView = menu.findItem(R.id.menu_cart).actionView
            if (itemQuantity > 0) {
                menuCartView.text_view_cart_count.isVisible = true
                menuCartView.text_view_cart_count.text = itemQuantity.toString()
            } else
                menuCartView.text_view_cart_count.isVisible = false
            return true
        }

        fun menuOptionSelected(mContext: Context, item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> if (mContext !is HomeActivity) (mContext as Activity).finish()
                R.id.menu_cart -> mContext.startActivity(Intent(mContext, CartActivity::class.java))
                R.id.menu_home -> {
                    mContext.startActivity(Intent(mContext, HomeActivity::class.java))
                    (mContext as Activity).finishAffinity()
                }
            }
            return true
        }

        fun navigationMenuInit(menu: Menu) {
            if (SessionManager.isLoggedIn) {
                menu.findItem(R.id.menu_login).isVisible = false
            } else
                menu.findItem(R.id.menu_logout).isVisible = false
        }
    }
}