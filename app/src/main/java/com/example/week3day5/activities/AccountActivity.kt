package com.example.week3day5.activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.tool_bar.*

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.TITLE_ACCOUNT
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val user = SessionManager.userLoggedIn
        text_view_name.text = user.firstName
        text_view_email.text = user.email
        text_view_mobile.text = user.mobile
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }
}