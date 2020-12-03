package com.example.week3day5.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.week3day5.R
import com.example.week3day5.app.Config
import com.example.week3day5.app.EndPoints
import com.example.week3day5.helpers.MenuManager
import com.example.week3day5.helpers.SessionManager
import com.example.week3day5.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        //toolbar
        tool_bar.title = Config.TITLE_LOGIN
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //log in
        btn_login.setOnClickListener {
            val email = edit_text_email.text.toString()
            val pw = edit_text_pw.text.toString()

            val request = JsonObjectRequest(
                Request.Method.POST,
                EndPoints.getLoginPostUrl(),
                User.convertToJsonObj("", email, pw, ""),
                {
                    val user = Gson().fromJson(it.getString(User.KEY_USER), User::class.java)
                    user.setName()
                    val sm = SessionManager(this)
                    sm.addUserToSp(user, it.getString(User.KEY_TOKEN))

                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                },
                {
                    val response = String(it.networkResponse.data)
                    Toast.makeText(applicationContext, JSONObject(response).getString(Config.KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show()
                }
            )
            Volley.newRequestQueue(this).add(request)
        }
        //to register
        btn_login_to_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return MenuManager.menuOptionSelected(this, item)
    }
}